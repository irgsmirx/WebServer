/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ramforth.webserver.http.parsers;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.ramforth.webserver.exceptions.HttpException;
import com.ramforth.webserver.http.HttpStatusCode;
import com.ramforth.webserver.http.IMediaType;
import com.ramforth.webserver.http.headers.entity.ContentLengthHttpHeader;
import com.ramforth.webserver.http.headers.entity.ContentTypeHttpHeader;
import com.ramforth.webserver.http.headers.general.TransferEncodingHttpHeader;
import java.util.Map;
import java.util.TreeMap;

/**
 *
 * @author tobias
 */
public class HttpRequestBodyParserFactory implements IHttpRequestBodyParserFactory {

    private final Map<String, IHttpRequestBodyParser> bodyParsers = new TreeMap<>();

    public HttpRequestBodyParserFactory() {
        bodyParsers.put("application/x-www-form-urlencoded", createApplicationXwwwFormUrlEncodedBodyParser());
        bodyParsers.put("application/json", createApplicationJsonBodyParser());
        bodyParsers.put("multipart/form-data", createMultipartFormDataBodyParser());
    }

    @Override
    public IHttpRequestBodyParser build(ContentTypeHttpHeader contentType, TransferEncodingHttpHeader transferEncoding) {
        IHttpRequestBodyParser bodyParser = null;

        if (contentType == null) {
            bodyParser = buildEmpty(transferEncoding);
        } else {
            bodyParser = buildFromContentType(contentType, transferEncoding);
        }
        
        bodyParser.setContentType(contentType);
        bodyParser.setTransferEncoding(transferEncoding);

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
            throw new HttpException(HttpStatusCode.STATUS_415_UNSUPPORTED_MEDIA_TYPE, "The media type '" + mimeTypeString + "' is not (yet) supported.");
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
}
