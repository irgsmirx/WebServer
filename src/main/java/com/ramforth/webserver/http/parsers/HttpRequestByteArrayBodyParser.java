/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ramforth.webserver.http.parsers;

import com.ramforth.webserver.exceptions.HttpException;
import com.ramforth.webserver.http.HttpRequestByteArrayBodyData;
import com.ramforth.webserver.http.HttpStatusCode;
import com.ramforth.webserver.http.IHttpRequestBodyData;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author tobias
 */
public class HttpRequestByteArrayBodyParser extends AbstractHttpRequestBodyParser {

    private static final Logger LOGGER = LoggerFactory.getLogger(HttpRequestByteArrayBodyParser.class);

    @Override
    public IHttpRequestBodyData parse(InputStream inputStream) {
        HttpBuffer buffer = new HttpBuffer();
        int written = 0;

        InputStreamReader isr = new InputStreamReader(inputStream, charset);

        int ch;
        try {
            while ((ch = isr.read()) != -1) {
                buffer.append(ch);
                written++;
            }
        }
        catch (IOException ex) {
            LOGGER.warn("Could not read plain content from client.", ex);
            throw new com.ramforth.webserver.exceptions.IOException(ex);
        }

        return new HttpRequestByteArrayBodyData(buffer.getCopy());
    }

}
