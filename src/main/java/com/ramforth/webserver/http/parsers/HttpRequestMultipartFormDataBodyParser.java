/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ramforth.webserver.http.parsers;

import com.ramforth.webserver.exceptions.HttpException;
import com.ramforth.webserver.http.HttpStatusCode;
import com.ramforth.webserver.http.IHttpHeaders;
import com.ramforth.webserver.http.IHttpRequestBodyData;
import com.ramforth.webserver.http.HttpRequestMultipartBodyData;
import com.ramforth.webserver.http.headers.entity.ContentTypeHttpHeader;
import com.ramforth.webserver.http.headers.general.ContentDispositionHttpHeader;
import static com.ramforth.webserver.http.parsers.HttpRequestParser.isCR;
import static com.ramforth.webserver.http.parsers.HttpRequestParser.isCTL;
import static com.ramforth.webserver.http.parsers.HttpRequestParser.isLF;
import static com.ramforth.webserver.http.parsers.HttpRequestParser.isSeparator;
import java.io.IOException;
import java.io.InputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Tobias Ramforth <tobias.ramforth at tu-dortmund.de>
 */
public class HttpRequestMultipartFormDataBodyParser extends AbstractHttpRequestBodyParser {

    private static final Logger LOGGER = LoggerFactory.getLogger(HttpRequestMultipartFormDataBodyParser.class);

    private int interBoundaryLength;
    private int lastBoundaryLength;
    private String interPartBoundary;
    private String lastBoundary;
    
    @Override
    public IHttpRequestBodyData parse(InputStream inputStream) {
        HttpRequestMultipartBodyData multipartBodyData = new HttpRequestMultipartBodyData();
        
        determineBoundary();
        
        boolean stillMorePartsToRead = true;
        
        while (stillMorePartsToRead) {
            tryReadInterBoundary(inputStream);

            try {
                int firstCharacter = inputStream.read();
                if (firstCharacter == -1) {
                    LOGGER.error("Could not read the first of two consecutive characters after inter boundary separator.");
                    throw new HttpException(HttpStatusCode.STATUS_400_BAD_REQUEST, "");
                }
                int secondCharacter = inputStream.read();
                if (secondCharacter == -1) {
                    LOGGER.error(String.format("Could not read the second of two consecutive characters after inter boundary separator. Already read: %s.", new String(new int[] { firstCharacter }, 0, 1)));
                    throw new HttpException(HttpStatusCode.STATUS_400_BAD_REQUEST, "");
                }
                if (isCR(firstCharacter) && isLF(secondCharacter)) {
                    stillMorePartsToRead = true;
                } else if (firstCharacter == '-' && secondCharacter == '-') {
                    stillMorePartsToRead = false;
                    break;
                } else {
                    LOGGER.error(String.format("Read the two consecutive characters after inter boundary separator. Could have been either CRLF or DASHDASH but was: %s.", new String(new int[] { firstCharacter, secondCharacter }, 0, 2)));
                    throw new HttpException(HttpStatusCode.STATUS_400_BAD_REQUEST, "");
                }
            } catch (IOException ioex) {
                LOGGER.error("Tried to read the two consecutive characters after inter boundary separator. Could have been either CRLF or DASHDASH.", ioex);
                throw new HttpException(HttpStatusCode.STATUS_400_BAD_REQUEST, ioex.getMessage());
            }

            IHttpHeadersParser headersParser = new HttpHeadersParser();
            IHttpHeaders headers = headersParser.parse(inputStream);

            ContentDispositionHttpHeader contentDisposition = (ContentDispositionHttpHeader) headers.getHeader("Content-Disposition");
            if (contentDisposition == null 
                    || !contentDisposition.getDispositionType().getType().equalsIgnoreCase("form-data")
                    || !contentDisposition.getDispositionType().getParameters().containsName("name")) {
                throw new HttpException(HttpStatusCode.STATUS_400_BAD_REQUEST,
                        String.format("Bad Content-Disposition: %s.", contentType.getRawValue()));
            }
            
            ContentTypeHttpHeader partContentType = (ContentTypeHttpHeader) headers.getHeader("Content-Type");
            
            IHttpRequestBodyParserFactory bodyParserFactory = new HttpRequestBodyParserFactory();
            IHttpRequestBodyParser partBodyParser = bodyParserFactory.build(partContentType, contentDisposition);
            
            IHttpRequestBodyData partBodyData = partBodyParser.parse(inputStream);
            multipartBodyData.addPart(partBodyData);
        }
        
        return multipartBodyData;
    }
    
    private void determineBoundary() {
        String boundary = contentType.getMediaType().getParameters().get("boundary");
        
        if (boundary == null) {
            throw new HttpException(HttpStatusCode.STATUS_400_BAD_REQUEST,
                    String.format("Header field Content-Type was 'multipart/form-data' but did not contain boundary parameter: '%1s'.", contentType.getRawValue()));
        }
        
        this.interPartBoundary = String.format("--%s", boundary);
        this.interBoundaryLength = interPartBoundary.length();
        this.lastBoundary = String.format("--%s--", boundary);
        this.lastBoundaryLength = lastBoundary.length();
    }
    
    private void tryReadInterBoundary(InputStream is) {
        try {
            int position = 0;
            int ch = -1;
            while (position < interBoundaryLength && (ch = is.read()) != -1) {
                if (ch != interPartBoundary.codePointAt(position)) {
                    LOGGER.warn("Error while reading inter part boundary. Expected '" + interPartBoundary.codePointAt(position) + "' but read '" + ch + "' instead.");
                    throw new com.ramforth.webserver.exceptions.IOException("");
                }
                position++;
            }
            if (position < interBoundaryLength || ch == -1) {
                throw new com.ramforth.webserver.exceptions.IOException("");
            }
        } catch (IOException ex) {
                        LOGGER.warn("Preliminary end of input in header key name.", ex);
                        throw new com.ramforth.webserver.exceptions.IOException(ex);
        }
    }
    
    private HttpBuffer readLine(InputStream is) {
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
                    throw new HttpException(HttpStatusCode.STATUS_400_BAD_REQUEST, "Your HTTP client's request ended unexpectedly.");
                } else if (isCTL(ch)) {
                    throw new HttpException(HttpStatusCode.STATUS_400_BAD_REQUEST,
                            String.format("Your HTTP client's request header contained an unallowed CTL character: '%1s'.", (char) ( ch & 0xff )));
                } else if (isSeparator(ch)) {
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

    
}
