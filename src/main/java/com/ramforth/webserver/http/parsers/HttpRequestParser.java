/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ramforth.webserver.http.parsers;

import com.ramforth.webserver.exceptions.HttpException;
import com.ramforth.webserver.http.*;
import com.ramforth.webserver.http.headers.entity.ContentLengthHttpHeader;
import com.ramforth.webserver.http.headers.entity.ContentTypeHttpHeader;
import com.ramforth.webserver.http.headers.general.TransferEncodingHttpHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

/**
 *
 * @author Tobias Ramforth <tobias.ramforth at tu-dortmund.de>
 */
public class HttpRequestParser implements IHttpRequestParser {

    private static final Logger LOGGER = LoggerFactory.getLogger(HttpRequestParser.class);

    private final IHttpRequestLineParser requestLineParser = new HttpRequestLineParser();
    private final IHttpHeadersParser headersParser = new HttpHeadersParser();
    private final IHttpRequestBodyParserFactory bodyParserFactory = new HttpRequestBodyParserFactory();

    @Override
    public IHttpRequest parseRequest(InputStream is) {
        IHttpRequest httpRequest = null;
        try {
            IHttpRequestLine requestLine = requestLineParser.parse(is);
            IHttpHeaders httpHeaders = headersParser.parse(is);

            if (requestLine.getVersion().isHTTP11() && !httpHeaders.contains("host")) {
                throw new HttpException(HttpStatusCode.STATUS_400_BAD_REQUEST, "HTTP/1.1 request without host header.");
            }

            httpRequest = new HttpRequest(httpHeaders);

            httpRequest.setMethod(requestLine.getMethod());
            httpRequest.setVersion(requestLine.getVersion());
            httpRequest.setUri(requestLine.getURI());

            if (requestLine.getURI().getQuery() != null) {
                fillQueryString(httpRequest, requestLine.getURI().getQuery(), true);
            }

            ContentLengthHttpHeader contentLengthHeader = ((ContentLengthHttpHeader) httpHeaders.getHeader("Content-Length"));
            ContentTypeHttpHeader contentTypeHeader = ((ContentTypeHttpHeader) httpHeaders.getHeader("Content-Type"));

            IHttpHeader transferEncoding = httpHeaders.getHeader("Transfer-Encoding");
            TransferEncodingHttpHeader transferEncodingHeader = null;
            if (transferEncoding != null) {
                transferEncodingHeader = (TransferEncodingHttpHeader) transferEncoding;
            }

            IHttpRequestBodyParser bodyParser = bodyParserFactory.build(contentTypeHeader, transferEncodingHeader);
            IHttpRequestBodyData bodyData = null;
            if (contentLengthHeader == null) {
                if (transferEncodingHeader != null && transferEncodingHeader.getValue().equalsIgnoreCase("chunked")) {
                    ChunkedInputStream cis = new ChunkedInputStream(is, httpHeaders);
                    bodyData = bodyParser.parse(cis);
                } else {
                    bodyData = bodyParser.parse(is);
                }
            } else {
                InputStream contentLengthInputStream = new ContentLengthInputStream(is, contentLengthHeader.getValue());
                if (transferEncodingHeader != null && transferEncodingHeader.getValue().equalsIgnoreCase("chunked")) {
                    ChunkedInputStream cis = new ChunkedInputStream(contentLengthInputStream, httpHeaders);
                    bodyData = bodyParser.parse(cis);
                } else {
                    bodyData = bodyParser.parse(contentLengthInputStream);
                }
            }
            bodyData.applyTo(httpRequest);
        } catch (HttpException ex) {
            LOGGER.warn("Probably malformed HTTP request.", ex);
        }

        return httpRequest;
    }

    public void fillQueryString(IHttpRequest request, String queryString, boolean urlEncoded) {
        request.getQueryString().addAll(new ParameterParser().parse(queryString, urlEncoded));
    }

    @Override
    public byte[] parseBody(InputStream is, IHttpHeaders headers) {
        ContentLengthHttpHeader contentLengthHeader = ((ContentLengthHttpHeader) headers.getHeader("Content-Length"));
        ContentTypeHttpHeader contentTypeHeader = ((ContentTypeHttpHeader) headers.getHeader("Content-Type"));

        IHttpHeader transferEncoding = headers.getHeader("Transfer-Encoding");
        TransferEncodingHttpHeader transferEncodingHeader = null;
        if (transferEncoding != null) {
            transferEncodingHeader = (TransferEncodingHttpHeader) transferEncoding;
        }

        String contentType = contentTypeHeader.getMediaType().getType();
        Charset charset = contentTypeHeader.getMediaType().getCharset();

        if (transferEncodingHeader == null || transferEncodingHeader.getValue() == null) {
            if (contentLengthHeader == null) {
                // well, no body present, as it seems
                return new byte[0];
            } else {
                if (contentType.compareTo("application/x-www-form-urlencoded") == 0
                        || contentType.compareTo("application/json") == 0) {
                    try {
                        long contentLength = contentLengthHeader.getValue();
                        return readPlain(is, contentLength, charset);
                    }
                    catch (NumberFormatException e) {
                        throw new HttpException(HttpStatusCode.STATUS_400_BAD_REQUEST,
                                String.format("Header field Content-Length contained invalid value '%1s'.", contentLengthHeader.getRawValue()));
                    }
                } else if (contentType.compareTo("multipart/form-data") == 0) {
                    if (!contentTypeHeader.getMediaType().getParameters().containsName("boundary")) {
                        throw new HttpException(HttpStatusCode.STATUS_400_BAD_REQUEST,
                                String.format("Header field Content-Type was 'multipart/form-data' but did not contain boundary parameter: '%1s'.", contentTypeHeader.getRawValue()));
                    }

                    String boundary = contentTypeHeader.getMediaType().getParameters().get("boundary");

                    readUntilBoundary(is, boundary, charset);

                    throw new HttpException(HttpStatusCode.STATUS_415_UNSUPPORTED_MEDIA_TYPE, "Media type is not supported.");
                } else {
                    throw new HttpException(HttpStatusCode.STATUS_415_UNSUPPORTED_MEDIA_TYPE, "Media type is not supported.");
                }
            }
        } else {
            if (transferEncodingHeader.getValue().compareTo("chunked") == 0) {
                return readChunked(is, headers);
            } else {
                throw new HttpException(HttpStatusCode.STATUS_501_NOT_IMPLEMENTED,
                        String.format("The Transfer-Encoding '%1s' is not supported.", transferEncoding));
            }
        }
    }

    protected byte[] readPlain(InputStream is, long contentLength, Charset charset) {
        HttpBuffer buffer = new HttpBuffer();
        int written = 0;

        InputStreamReader isr = new InputStreamReader(is, charset);

        int ch;
        try {
            while (written < contentLength && (ch = isr.read()) != -1) {
                buffer.append(ch);
                written++;
            }
        }
        catch (IOException ex) {
            LOGGER.warn("Could not read plain content from client.", ex);
            throw new com.ramforth.webserver.exceptions.IOException(ex);
        }

        if (written < contentLength) {
            throw new HttpException(HttpStatusCode.STATUS_400_BAD_REQUEST, "Unexpected end of stream.");
        }

        return buffer.getCopy();
    }

    protected byte[] readUntilBoundary(InputStream is, String boundary, Charset charset) {
        HttpBuffer buffer = new HttpBuffer();
        int written = 0;

        InputStreamReader isr = new InputStreamReader(is, charset);

        int ch;
        try {
            while ((ch = isr.read()) != -1) {
                buffer.append(ch);
                written++;
            }
        }
        catch (IOException ex) {
            LOGGER.warn("Could not read boundary content from client.", ex);
            throw new com.ramforth.webserver.exceptions.IOException(ex);
        }

        return buffer.getCopy();
    }

    protected byte[] readChunked(InputStream is, IHttpHeaders headers) {
        HttpBuffer buffer = new HttpBuffer();
        ChunkedInputStream cis = new ChunkedInputStream(is, headers);

        byte[] body;
        int ch;
        try {
            ch = cis.read();
        }
        catch (IOException ex) {
            LOGGER.warn("Could not read plain content from client.", ex); //TODO Enter precise error message
            throw new com.ramforth.webserver.exceptions.IOException(ex);
        }

        while (true) {
            if (ch == -1) {
                break;
            } else {
                buffer.append(ch);
            }
        }

        return buffer.getCopy();
    }

    public static boolean isCHAR(int ch) {
        return (ch >= 0 && ch <= 127);
    }

    public static boolean isUPALPHA(int ch) {
        return (ch >= 65 && ch <= 90);
    }

    public static boolean isLOALPHA(int ch) {
        return (ch >= 97 && ch <= 122);
    }

    public static boolean isALPHA(int ch) {
        return (isUPALPHA(ch) || isLOALPHA(ch));
    }

    public static boolean isCTL(int ch) {
        return (ch >= 0 && ch <= 31 || ch == 127);
    }

    public static boolean isDIGIT(int ch) {
        return (ch >= 48 && ch <= 57);
    }

    public static boolean isCR(int ch) {
        return (ch == 13);
    }

    public static boolean isLF(int ch) {
        return (ch == 10);
    }

    public static boolean isSP(int ch) {
        return (ch == 32);
    }

    public static boolean isHT(int ch) {
        return (ch == 9);
    }

    public static boolean isLWS(int ch) {
        return isSP(ch) || isHT(ch);
    }

    public static boolean isDoubleQuoteMark(int ch) {
        return (ch == 34);
    }

    public static boolean isSeparator(int ch) {
        return (ch == '(' || ch == ')' || ch == '<' || ch == '>' || ch == '@' || ch == ',' || ch == ';' || ch == ':'
                || ch == '\\' || ch == '\"' || ch == '/' || ch == '[' || ch == ']' || ch == '?' || ch == '=' || ch == '{'
                || ch == '}' || ch == ' ' || ch == '\t');
    }
}
