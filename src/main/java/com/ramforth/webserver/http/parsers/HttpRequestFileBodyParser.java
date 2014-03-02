/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ramforth.webserver.http.parsers;

import com.ramforth.webserver.http.HttpRequestByteArrayBodyData;
import com.ramforth.webserver.http.HttpRequestFileBodyData;
import com.ramforth.webserver.http.IHttpRequestBodyData;
import java.io.InputStream;

/**
 *
 * @author tobias
 */
public class HttpRequestFileBodyParser extends AbstractHttpRequestBodyParser {

    @Override
    public IHttpRequestBodyData parse(InputStream inputStream) {
        IHttpRequestBodyParser byteArrayBodyParser = new HttpRequestByteArrayBodyParser();
        byteArrayBodyParser.setContentType(contentType);
        byteArrayBodyParser.setContentDisposition(contentDisposition);
        byteArrayBodyParser.setCharset(charset);
        byteArrayBodyParser.setTransferEncoding(transferEncoding);

        HttpRequestByteArrayBodyData byteArrayBodyData = (HttpRequestByteArrayBodyData) byteArrayBodyParser.parse(inputStream);

        String filename = contentDisposition.getDispositionType().getValue("filename");
        String name = contentDisposition.getDispositionType().getValue("name");
        String mimeType = contentType.getMediaType().getType();

        return new HttpRequestFileBodyData(name, filename, mimeType, byteArrayBodyData.getData());
    }

}
