/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ramforth.webserver.http;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author tobias
 */
public class HttpRequestMultipartBodyData implements IHttpRequestBodyData {

    private final List<IHttpRequestBodyData> parts = new ArrayList<>();

    @Override
    public void applyTo(IHttpRequest httpRequest) {
        for (IHttpRequestBodyData part : parts) {
            part.applyTo(httpRequest);
        }
    }

    public final Iterable<IHttpRequestBodyData> getParts() {
        return parts;
    }

    public void addPart(IHttpRequestBodyData part) {
        this.parts.add(part);
    }

}
