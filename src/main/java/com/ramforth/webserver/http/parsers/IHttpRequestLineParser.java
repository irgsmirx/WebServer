/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ramforth.webserver.http.parsers;

import com.ramforth.webserver.http.IHttpRequestLine;
import java.io.InputStream;

/**
 *
 * @author tobias
 */
public interface IHttpRequestLineParser {

    IHttpRequestLine parse(InputStream is);

}
