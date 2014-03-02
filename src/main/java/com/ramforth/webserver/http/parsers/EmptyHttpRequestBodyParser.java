/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ramforth.webserver.http.parsers;

import com.ramforth.webserver.http.EmptyHttpRequestBodyData;
import com.ramforth.webserver.http.IHttpRequestBodyData;
import java.io.InputStream;

/**
 *
 * @author tobias
 */
public class EmptyHttpRequestBodyParser extends AbstractHttpRequestBodyParser {

    @Override
    public IHttpRequestBodyData parse(InputStream inputStream) {
        return new EmptyHttpRequestBodyData();
    }

}
