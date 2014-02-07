/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ramforth.webserver.http.parsers;

import com.ramforth.webserver.exceptions.HttpException;
import com.ramforth.webserver.http.HttpMethod;
import com.ramforth.webserver.http.HttpRequestLine;
import com.ramforth.webserver.http.HttpStatusCode;
import com.ramforth.webserver.http.HttpUtils;
import com.ramforth.webserver.http.HttpVersion;
import com.ramforth.webserver.http.IHttpRequestLine;
import com.ramforth.webserver.http.IHttpVersion;
import static com.ramforth.webserver.http.parsers.HttpRequestParser.isCR;
import static com.ramforth.webserver.http.parsers.HttpRequestParser.isLF;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author tobias
 */
public class HttpRequestLineParser implements IHttpRequestLineParser {

    private static final Logger LOGGER = LoggerFactory.getLogger(HttpRequestLineParser.class);

    private boolean isLegalHttpMethod(HttpMethod method) {
        switch (method) {
            case OPTIONS:
            case GET:
            case HEAD:
            case POST:
                return true;
            default:
            case PUT:
            case DELETE:
            case TRACE:
            case CONNECT:
                return false;
        }
    }

    @Override
    public IHttpRequestLine parse(InputStream is) {
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

            try {
                method = Enum.valueOf(HttpMethod.class, methodString);
            }
            catch (IllegalArgumentException iaex) {
                throw new HttpException(HttpStatusCode.STATUS_501_NOT_IMPLEMENTED, "Method '" + methodString + "' not implemented!");
            }
            catch (NullPointerException npex) {
                throw new HttpException(HttpStatusCode.STATUS_501_NOT_IMPLEMENTED, "Method '" + methodString + "' not implemented!");
            }

            if (!isLegalHttpMethod(method)) {
                throw new HttpException(HttpStatusCode.STATUS_405_METHOD_NOT_ALLOWED, "Method '" + methodString + "' not allowed!");
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

}
