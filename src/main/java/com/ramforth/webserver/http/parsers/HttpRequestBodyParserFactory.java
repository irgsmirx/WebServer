/* 
 * Copyright (C) 2014 Tobias Ramforth
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.ramforth.webserver.http.parsers;

import com.ramforth.webserver.exceptions.HttpException;
import com.ramforth.webserver.http.HttpStatusCode;
import com.ramforth.webserver.http.IMediaType;
import com.ramforth.webserver.http.headers.entity.ContentTypeHttpHeader;
import com.ramforth.webserver.http.headers.general.ContentDispositionHttpHeader;
import com.ramforth.webserver.http.headers.general.TransferEncodingHttpHeader;
import java.util.Map;
import java.util.TreeMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author tobias
 */
public class HttpRequestBodyParserFactory implements IHttpRequestBodyParserFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(HttpRequestBodyParserFactory.class);

    private final Map<String, IHttpRequestBodyParser> bodyParsers = new TreeMap<>();

    public HttpRequestBodyParserFactory() {
        bodyParsers.put("application/x-www-form-urlencoded", createApplicationXwwwFormUrlEncodedBodyParser());
        bodyParsers.put("application/json", createApplicationJsonBodyParser());
        bodyParsers.put("multipart/form-data", createMultipartFormDataBodyParser());
        bodyParsers.put("text/plain", createTextPlainBodyParser());
        bodyParsers.put("text/xml", createTextXmlBodyParser());
    }

    @Override
    public IHttpRequestBodyParser build(ContentTypeHttpHeader contentType, TransferEncodingHttpHeader transferEncoding) {
        IHttpRequestBodyParser bodyParser = null;

        if (contentType == null) {
            bodyParser = createTextPlainBodyParser();
        } else {
            bodyParser = buildFromContentType(contentType, transferEncoding);
            bodyParser.setCharset(contentType.getMediaType().getCharset());
        }

        bodyParser.setContentType(contentType);
        bodyParser.setTransferEncoding(transferEncoding);

        return bodyParser;
    }

    @Override
    public IHttpRequestBodyParser build(ContentTypeHttpHeader contentType, ContentDispositionHttpHeader contentDisposition) {
        IHttpRequestBodyParser bodyParser = null;

        if (contentDisposition == null) {
            if (contentType == null) {
                bodyParser = createTextPlainBodyParser();
            } else {
                bodyParser = buildFromContentType(contentType, null);
                bodyParser.setCharset(contentType.getMediaType().getCharset());
            }
        } else {
            bodyParser = new HttpRequestFileBodyParser();
            bodyParser.setContentDisposition(contentDisposition);
            if (contentType != null) {
                bodyParser.setCharset(contentType.getMediaType().getCharset());
            }
        }

        bodyParser.setContentType(contentType);

        return bodyParser;
    }

    private IHttpRequestBodyParser buildEmpty(TransferEncodingHttpHeader transferEncoding) {
        return new EmptyHttpRequestBodyParser();
    }

    private IHttpRequestBodyParser buildFromContentType(ContentTypeHttpHeader contentType, TransferEncodingHttpHeader transferEncoding) {
        IMediaType mediaType = contentType.getMediaType();
        String mimeTypeString = mediaType.getType();
        String loweredMimeTypeString = mimeTypeString.toLowerCase();

        IHttpRequestBodyParser bodyParser = bodyParsers.get(loweredMimeTypeString);
        if (bodyParser == null) {
            LOGGER.warn("Unsupported media type. Trying application/octet-stream.", new HttpException(HttpStatusCode.STATUS_415_UNSUPPORTED_MEDIA_TYPE, "The media type '" + mimeTypeString + "' is not (yet) supported."));
            bodyParser = new HttpRequestFileBodyParser();
        }
        return bodyParser;
    }

    private IHttpRequestBodyParser createApplicationXwwwFormUrlEncodedBodyParser() {
        return new HttpRequestWwwFormUrlEncodedBodyParser();
    }

    private IHttpRequestBodyParser createApplicationJsonBodyParser() {
        return new HttpRequestApplicationJsonBodyParser();
    }

    private IHttpRequestBodyParser createMultipartFormDataBodyParser() {
        return new HttpRequestMultipartFormDataBodyParser();
    }

    private IHttpRequestBodyParser createTextPlainBodyParser() {
        return new HttpRequestTextPlainBodyParser();
    }

    private IHttpRequestBodyParser createTextXmlBodyParser() {
        return new HttpRequestTextXmlBodyParser();
    }
}
