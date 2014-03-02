/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ramforth.webserver.http;

/**
 *
 * @author tobias
 */
public class HttpRequestByteArrayBodyData implements IHttpRequestBodyData {

    private final byte[] data;

    public HttpRequestByteArrayBodyData(byte[] data) {
        this.data = data;
    }

    @Override
    public void applyTo(IHttpRequest httpRequest) {
        ((HttpRequest) httpRequest).setBody(data);
    }

    public final byte[] getData() {
        return data;
    }

}
