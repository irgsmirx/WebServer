/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ramforth.webserver.http;

import java.net.URI;

/**
 *
 * @author Tobias Ramforth <tobias.ramforth at tu-dortmund.de>
 */
public interface IHttpRequestLine {

    HttpMethod getMethod();

    void setMethod(HttpMethod value);

    IHttpVersion getVersion();

    void setVersion(IHttpVersion value);

    URI getURI();

    void setURI(URI value);
}
