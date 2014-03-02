/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ramforth.webserver.http.parsers;

import com.ramforth.webserver.http.IHttpRequestBodyData;
import java.io.InputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Tobias Ramforth <tobias.ramforth at tu-dortmund.de>
 */
public class HttpRequestTextPlainBodyParser extends AbstractHttpRequestBodyParser {

    private static final Logger LOGGER = LoggerFactory.getLogger(HttpRequestTextPlainBodyParser.class);

    @Override
    public IHttpRequestBodyData parse(InputStream inputStream) {
        IHttpRequestBodyParser byteArrayParser = new HttpRequestByteArrayBodyParser();
        byteArrayParser.setTransferEncoding(transferEncoding);
        byteArrayParser.setContentType(contentType);
        byteArrayParser.setCharset(charset);

        return byteArrayParser.parse(inputStream);
    }

}
