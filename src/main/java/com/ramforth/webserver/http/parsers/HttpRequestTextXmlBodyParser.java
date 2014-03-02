/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ramforth.webserver.http.parsers;

import com.ramforth.webserver.http.IHttpRequestBodyData;
import java.io.InputStream;

/**
 *
 * @author Tobias Ramforth <tobias.ramforth at tu-dortmund.de>
 */
public class HttpRequestTextXmlBodyParser extends AbstractHttpRequestBodyParser {

    @Override
    public IHttpRequestBodyData parse(InputStream inputStream) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
