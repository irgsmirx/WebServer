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
public class HttpRequestFormBodyData implements IHttpRequestBodyData {

    private final NameValueMap formData;

    public HttpRequestFormBodyData(NameValueMap formData) {
        this.formData = formData;
    }

    @Override
    public void applyTo(IHttpRequest httpRequest) {
        httpRequest.getForm().addAll(formData);
    }

}
