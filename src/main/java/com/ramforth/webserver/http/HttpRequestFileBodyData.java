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
public class HttpRequestFileBodyData implements IHttpRequestBodyData {

    private final String name;
    private final String filename;
    private final String mimeType;
    private final byte[] data;

    public HttpRequestFileBodyData(String name, String filename, String mimeType, byte[] data) {
        this.name = name;
        this.filename = filename;
        this.mimeType = mimeType;
        this.data = data;
    }

    @Override
    public void applyTo(IHttpRequest httpRequest) {
        HttpPostedFile httpPostedFile = new HttpPostedFile(this.name, this.filename, this.mimeType, this.data);
        ((HttpRequest) httpRequest).postedFiles.put(this.name, httpPostedFile);
    }

}
