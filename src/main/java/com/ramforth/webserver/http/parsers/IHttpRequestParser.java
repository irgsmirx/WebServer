/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ramforth.webserver.http.parsers;

import com.ramforth.webserver.http.IHttpHeaders;
import com.ramforth.webserver.http.IHttpRequest;
import java.io.InputStream;

/**
 *
 * @author Tobias Ramforth <tobias.ramforth at tu-dortmund.de>
 */
public interface IHttpRequestParser {

    IHttpRequest parseRequest(InputStream is);

    byte[] parseBody(InputStream is, IHttpHeaders headers);
    
}
