/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ramforth.webserver.http.parsers;

import com.ramforth.webserver.http.HttpRequestBodyData;
import java.io.InputStream;

/**
 *
 * @author Tobias Ramforth <tobias.ramforth at tu-dortmund.de>
 */
public class HttpRequestApplicationJsonBodyParser implements IHttpRequestBodyParser {

    @Override
    public HttpRequestBodyData parse(InputStream inputStream) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
