/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ramforth.webserver.http.parsers;

import com.ramforth.webserver.exceptions.HttpException;
import com.ramforth.webserver.http.HttpHeaderFactory;
import com.ramforth.webserver.http.HttpHeaders;
import com.ramforth.webserver.http.HttpStatusCode;
import com.ramforth.webserver.http.IHttpHeader;
import com.ramforth.webserver.http.IHttpHeaderFactory;
import com.ramforth.webserver.http.IHttpHeaders;
import static com.ramforth.webserver.http.parsers.HttpRequestParser.isCR;
import static com.ramforth.webserver.http.parsers.HttpRequestParser.isCTL;
import static com.ramforth.webserver.http.parsers.HttpRequestParser.isLF;
import static com.ramforth.webserver.http.parsers.HttpRequestParser.isLWS;
import static com.ramforth.webserver.http.parsers.HttpRequestParser.isSeparator;
import java.io.IOException;
import java.io.InputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author tobias
 */
public class HttpHeadersParser implements IHttpHeadersParser {

    private static final Logger LOGGER = LoggerFactory.getLogger(HttpHeadersParser.class);

    private final IHttpHeaderFactory httpHeaderFactory = new HttpHeaderFactory();
    
    @Override
    public IHttpHeaders parse(InputStream is) {
        IHttpHeaders httpHeaders = new HttpHeaders();

        IHttpHeader header;
        while (( header = parseHeader(is) ) != null) {
            httpHeaders.addHeader(header);
        }

        return httpHeaders;
    }

    public IHttpHeader parseHeader(InputStream is) {
        IHttpHeader header;

        HttpBuffer keyBuffer = readHeaderKey(is);

        if (keyBuffer.length == 0) {
            header = null;
        } else {
            String key = keyBuffer.toString().trim();
            HttpBuffer valueBuffer = readHeaderValue(is);
            String rawValue = valueBuffer.toString().trim();

            header = httpHeaderFactory.buildHttpHeader(key, rawValue);
        }

        return header;
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


    
}
