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
public class HttpRequestLine implements IHttpRequestLine {

    private HttpMethod method;
    private IHttpVersion version;
    private URI uri;

    public HttpRequestLine(HttpMethod method, IHttpVersion version, URI uri) {
        this.method = method;
        this.version = version;
        this.uri = uri;
    }

    @Override
    public HttpMethod getMethod() {
        return method;
    }

    @Override
    public void setMethod(HttpMethod value) {
        this.method = value;
    }

    @Override
    public IHttpVersion getVersion() {
        return version;
    }

    @Override
    public void setVersion(IHttpVersion value) {
        this.version = value;
    }

    @Override
    public URI getURI() {
        return uri;
    }

    @Override
    public void setURI(URI value) {
        this.uri = value;
    }
}
