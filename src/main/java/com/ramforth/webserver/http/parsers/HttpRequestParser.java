/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ramforth.webserver.http.parsers;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
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
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.nio.charset.Charset;

/**
 *
 * @author Tobias Ramforth <tobias.ramforth at tu-dortmund.de>
 */
public class HttpRequestParser implements IHttpRequestParser {

    private static final Logger LOGGER = LoggerFactory.getLogger(HttpRequestParser.class);

    private IHttpHeaderFactory httpHeaderFactory = new HttpHeaderFactory();

    @Override
    public IHttpRequest parseRequest(InputStream is) {
        IHttpRequest httpRequest = null;
        try {
            IHttpRequestLine requestLine = parseRequestLine(is);
            IHttpHeaders httpHeaders = parseHeaders(is);

            if (requestLine.getVersion().isHTTP11() && !httpHeaders.contains("host")) {
                throw new HttpException(HttpStatusCode.STATUS_400_BAD_REQUEST, "HTTP/1.1 request without host header.");
            }

            byte[] bodyBytes = null;
            try {
                bodyBytes = parseBody(is, httpHeaders);
            }
            catch (Exception ex) {
                // temporarily not handled because parseBody is not working as it should
            }

            httpRequest = new HttpRequest(httpHeaders);

            httpRequest.setMethod(requestLine.getMethod());
            httpRequest.setVersion(requestLine.getVersion());
            httpRequest.setUri(requestLine.getURI());

            if (requestLine.getURI().getQuery() != null) {
                fillQueryString(httpRequest, requestLine.getURI().getQuery(), true);
            }

            if (bodyBytes != null && bodyBytes.length > 0) {
                ContentTypeHttpHeader contentTypeHeader = ( (ContentTypeHttpHeader) httpHeaders.getHeader("Content-Type") );

                if (contentTypeHeader != null) {
                    if (contentTypeHeader.getMediaType().getType().equalsIgnoreCase("application/x-www-form-urlencoded")) {
                        fillForm(httpRequest, new String(bodyBytes, contentTypeHeader.getCharset()));
                    } else if (contentTypeHeader.getValue().equalsIgnoreCase("application/json")) {
                        JsonParser jsonParser = new JsonParser();
                        JsonObject jsonObject = (JsonObject)jsonParser.parse(new String(bodyBytes, contentTypeHeader.getCharset()));
                        fillJSON(httpRequest, jsonObject);
                    }
                }
            }
        }
        catch (HttpException ex) {
            LOGGER.warn("Probably malformed HTTP request.", ex);
        }

        return httpRequest;
    }

    public NameValueMap parseParameters(IHttpRequest request, String parameterString, boolean urlEncoded) {
        NameValueMap result = new NameValueMap();

        if (parameterString == null) {
            return result;
        }

        int queryStringLength = parameterString.length();
        for (int i = 0; i < queryStringLength; i++) {
            int startOfCurrentParameter = i;
            int indexOfEqualsSign = -1;
            while (i < queryStringLength) {
                int c = parameterString.codePointAt(i);
                if (c == '=') {
                    if (indexOfEqualsSign < 0) {
                        indexOfEqualsSign = i;
                    }
                } else {
                    if (c == '&') {
                        break;
                    }
                }
                i++;
            }

            String name = null;
            String value;
            if (indexOfEqualsSign >= 0) {
                name = parameterString.substring(startOfCurrentParameter, indexOfEqualsSign);
                value = parameterString.substring(indexOfEqualsSign + 1, i);
            } else {
                value = parameterString.substring(startOfCurrentParameter, i);
            }

            if (urlEncoded) {
                try {
                    result.add(URLDecoder.decode(name, "UTF-8"), URLDecoder.decode(value, "UTF-8"));
                }
                catch (UnsupportedEncodingException ex) {
                    LOGGER.warn("Encoding UTF-8 not supported.", ex);
                }
            } else {
                result.add(name, value);
            }

            if (i == queryStringLength - 1 && parameterString.codePointAt(i) == '&') {
                //base.Add(null, string.Empty);
            }
        }

        return result;
    }

    public void fillQueryString(IHttpRequest request, String queryString, boolean urlEncoded) {
        request.getQueryString().addAll(parseParameters(request, queryString, urlEncoded));
    }

    public void fillForm(IHttpRequest request, String form) {
        request.getForm().addAll(parseParameters(request, form, true));
    }
    
    public void fillJSON(IHttpRequest request, JsonObject jsonObject) {
        //request.setJSON(jsonObject);
    }

    @Override
    public IHttpHeaders parseHeaders(InputStream is) {
        IHttpHeaders httpHeaders = new HttpHeaders();

        IHttpHeader header;
        while (( header = parseHeader(is) ) != null) {
            httpHeaders.addHeader(header);
        }

        return httpHeaders;
    }

    @Override
    public IHttpHeader parseHeader(InputStream is) {
        IHttpHeader header;

        HttpBuffer keyBuffer = readHeaderKey(is);

        if (keyBuffer.length == 0) {
            header = null;
        } else {
            String key = keyBuffer.toString();
            HttpBuffer valueBuffer = readHeaderValue(is);
            String rawValue = valueBuffer.toString();

            header = httpHeaderFactory.buildHttpHeader(key, rawValue);
        }

        return header;
    }

    @Override
    public IHttpRequestLine parseRequestLine(InputStream is) {
        HttpBuffer requestLineBuffer = readRequestLine(is);
        String requestLine = requestLineBuffer.toString();

        String[] result = requestLine.split("\\s");

        HttpMethod method;
        IHttpVersion version;
        URI uri;

        if (result.length == 3) {
            String methodString = result[0].trim();
            String uriString = result[1].trim();
            String versionString = result[2].trim();

            switch (methodString) {
                case "OPTIONS":
                    method = HttpMethod.OPTIONS;
                    break;
                case "GET":
                    method = HttpMethod.GET;
                    break;
                case "HEAD":
                    method = HttpMethod.HEAD;
                    break;
                case "POST":
                    method = HttpMethod.POST;
                    break;
                case "PUT":
                    method = HttpMethod.PUT;
                    throw new HttpException(HttpStatusCode.STATUS_405_METHOD_NOT_ALLOWED, "Method '" + methodString + "' not allowed!");
                case "DELETE":
                    method = HttpMethod.DELETE;
                    throw new HttpException(HttpStatusCode.STATUS_405_METHOD_NOT_ALLOWED, "Method '" + methodString + "' not allowed!");
                case "TRACE":
                    method = HttpMethod.TRACE;
                    throw new HttpException(HttpStatusCode.STATUS_405_METHOD_NOT_ALLOWED, "Method '" + methodString + "' not allowed!");
                case "CONNECT":
                    method = HttpMethod.CONNECT;
                    throw new HttpException(HttpStatusCode.STATUS_405_METHOD_NOT_ALLOWED, "Method '" + methodString + "' not allowed!");
                default:
                    throw new HttpException(HttpStatusCode.STATUS_501_NOT_IMPLEMENTED, "Method '" + methodString + "' not implemented!");
            }

            try {
                uri = new URI(uriString);
            }
            catch (URISyntaxException e) {
                throw new HttpException(HttpStatusCode.STATUS_400_BAD_REQUEST, "URI '" + uriString + "' not valid!");
            }

            if (versionString.compareTo(HttpUtils.httpVersion(1, 0)) == 0) {
                version = new HttpVersion(1, 0);
            } else if (versionString.compareTo(HttpUtils.httpVersion(1, 1)) == 0) {
                version = new HttpVersion(1, 1);
            } else {
                throw new HttpException(HttpStatusCode.STATUS_505_HTTP_VERSION_NOT_SUPPORTED, "Version '" + versionString + "' not supported!");
            }
        } else {
            throw new HttpException(HttpStatusCode.STATUS_400_BAD_REQUEST, "Request line '" + requestLine + "' invalid!");
        }

        return new HttpRequestLine(method, version, uri);
    }

    private HttpBuffer readRequestLine(InputStream is) {
        HttpBuffer buffer = new HttpBuffer();

        int last = -1;
        int ch = -1;
        try {
            ch = is.read();
        }
        catch (IOException ex) {
            LOGGER.warn("Could not read request line from client!", ex);
            throw new com.ramforth.webserver.exceptions.IOException(ex);
        }

        while (isCR(ch) || isLF(ch)) {
            last = ch;
            try {
                ch = is.read();
            }
            catch (IOException ex) {
                LOGGER.warn("Could not read request line from client!", ex);
                throw new com.ramforth.webserver.exceptions.IOException(ex);
            }
        }

        while (true) {
            if (ch == -1) {
                // unepected end of input
                throw new HttpException(HttpStatusCode.STATUS_400_BAD_REQUEST, "Your HTTP client's request ended unexpectedly.");
            } else if (isCR(ch)) {
                last = ch;
            } else if (isLF(ch)) {
                if (isCR(last)) {
                    break;
                } else {
                    throw new HttpException(HttpStatusCode.STATUS_400_BAD_REQUEST,
                            "Your HTTP client's request contained an unallowed LF control character.");
                }
            } else {
                if (isCR(last)) {
                    throw new HttpException(HttpStatusCode.STATUS_400_BAD_REQUEST,
                            "Your HTTP client's request contained an unallowed LF control character.");
                } else {
                    last = ch;
                    buffer.append(ch);
                }
            }
            try {
                ch = is.read();
            }
            catch (IOException ex) {
                LOGGER.warn("Could not read after request line from client!", ex);
                throw new com.ramforth.webserver.exceptions.IOException(ex);
            }
        }

        return buffer;
    }

    protected HttpBuffer readHeaderKey(InputStream is) {
        HttpBuffer buffer = new HttpBuffer();

        int last = -1;
        int ch = -1;
        try {
            while (( ch = is.read() ) != ':') {
                if (isCR(ch)) {
                    try {
                        ch = is.read();
                    }
                    catch (IOException ex) {
                        LOGGER.warn("Preliminary end of input in header key name.", ex);
                        throw new com.ramforth.webserver.exceptions.IOException(ex);
                    }

                    if (isLF(ch)) {
                        break;
                    } else {
                        throw new HttpException(HttpStatusCode.STATUS_400_BAD_REQUEST, "Your HTTP client's request contained an unallowed CR control character.");
                    }
                } else if (ch == -1) {
                    // unexpected end of input
                    throw new HttpException(HttpStatusCode.STATUS_400_BAD_REQUEST, "Your HTTP client's request ended unexpectedly.");
                } else if (isCTL(ch)) {
                    // CTL character not allowed
                    throw new HttpException(HttpStatusCode.STATUS_400_BAD_REQUEST,
                            String.format("Your HTTP client's request header contained an unallowed CTL character: '%1s'.", (char) ( ch & 0xff )));
                } else if (isSeparator(ch)) {
                    // separator character not allowed
                    throw new HttpException(HttpStatusCode.STATUS_400_BAD_REQUEST,
                            String.format("Your HTTP client's request header contained an unallowed separator character: '%1s'.", (char) ( ch & 0xff )));
                } else {
                    buffer.append(ch);
                }
            }
        }
        catch (IOException ex) {
            LOGGER.warn("Could not read header key from client.", ex);
            throw new com.ramforth.webserver.exceptions.IOException(ex);
        }


        return buffer;
    }

    protected HttpBuffer readHeaderValue(InputStream is) {
        HttpBuffer buffer = new HttpBuffer();

        int lastCharacter = -1;
        int currentCharacter = -1;

        try {
            boolean insideQuote = false;
            boolean beforeValue = true;

            while (( currentCharacter = is.read() ) != -1) {
                if (beforeValue && isLWS(currentCharacter)) {
                    continue;
                } else if (isCR(currentCharacter)) {
                    beforeValue = false;
                    if (lastCharacter == '\\') {
                        if (insideQuote) {
                            buffer.append(currentCharacter);
                        }
                    }
                    lastCharacter = currentCharacter;
                } else if (isLF(currentCharacter)) {
                    beforeValue = false;
                    if (isCR(lastCharacter)) {
                        if (insideQuote) {
                            buffer.append('\r');
                            buffer.append('\n');
                        } else {
                            break;
                        }
                    } else {
                        if (insideQuote) {
                            // LF character not allowed in quoted text
                            throw new HttpException(HttpStatusCode.STATUS_400_BAD_REQUEST,
                                    "Your HTTP client's request header contained an LF character in quoted text, which is not allowed.");
                        } //TODO Enter precise error message
                    }
                } else if (currentCharacter == '\"') {
                    beforeValue = false;
                    if (lastCharacter == '\\') {
                        if (insideQuote) {
                            buffer.append(currentCharacter);
                        } else {
                            insideQuote = true;
                        }
                    } else {
                        insideQuote = !insideQuote;
                    }
                } else {
                    beforeValue = false;
                    lastCharacter = currentCharacter;
                    buffer.append(currentCharacter);
                }
            }
        }
        catch (IOException ex) {
            LOGGER.warn("Could not read header value from client.", ex);
            throw new com.ramforth.webserver.exceptions.IOException(ex);
        }

        return buffer;
    }

    @Override
    public byte[] parseBody(InputStream is, IHttpHeaders headers) {
        ContentLengthHttpHeader contentLengthHeader = ( (ContentLengthHttpHeader) headers.getHeader("Content-Length") );
        ContentTypeHttpHeader contentTypeHeader = ( (ContentTypeHttpHeader) headers.getHeader("Content-Type") );

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
            while (written < contentLength && ( ch = isr.read() ) != -1) {
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
        return ( ch >= 0 && ch <= 127 );
    }

    public static boolean isUPALPHA(int ch) {
        return ( ch >= 65 && ch <= 90 );
    }

    public static boolean isLOALPHA(int ch) {
        return ( ch >= 97 && ch <= 122 );
    }

    public static boolean isALPHA(int ch) {
        return ( isUPALPHA(ch) || isLOALPHA(ch) );
    }

    public static boolean isCTL(int ch) {
        return ( ch >= 0 && ch <= 31 || ch == 127 );
    }

    public static boolean isDIGIT(int ch) {
        return ( ch >= 48 && ch <= 57 );
    }

    public static boolean isCR(int ch) {
        return ( ch == 13 );
    }

    public static boolean isLF(int ch) {
        return ( ch == 10 );
    }

    public static boolean isSP(int ch) {
        return ( ch == 32 );
    }

    public static boolean isHT(int ch) {
        return ( ch == 9 );
    }

    public static boolean isLWS(int ch) {
        return isSP(ch) || isHT(ch);
    }

    public static boolean isDoubleQuoteMark(int ch) {
        return ( ch == 34 );
    }

    public static boolean isSeparator(int ch) {
        return ( ch == '(' || ch == ')' || ch == '<' || ch == '>' || ch == '@' || ch == ',' || ch == ';' || ch == ':'
                || ch == '\\' || ch == '\"' || ch == '/' || ch == '[' || ch == ']' || ch == '?' || ch == '=' || ch == '{'
                || ch == '}' || ch == ' ' || ch == '\t' );
    }
}
