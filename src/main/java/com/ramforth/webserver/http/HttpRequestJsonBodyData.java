/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ramforth.webserver.http;

import com.google.gson.JsonObject;

/**
 *
 * @author tobias
 */
public class HttpRequestJsonBodyData implements IHttpRequestBodyData {

    private final JsonObject jsonObject;

    public HttpRequestJsonBodyData(JsonObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    @Override
    public void applyTo(IHttpRequest httpRequest) {
        // FIXME: really apply
    }

}
