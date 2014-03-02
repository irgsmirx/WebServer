/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ramforth.webserver.http;

import com.ramforth.webserver.http.headers.HostHttpHeader;
import com.ramforth.webserver.http.headers.general.TransferEncodingHttpHeader;
import com.ramforth.webserver.http.headers.AjaxHttpHeader;
import com.ramforth.webserver.http.headers.entity.ContentTypeHttpHeader;
import com.ramforth.webserver.http.headers.StringHttpHeader;
import com.ramforth.webserver.http.headers.entity.ContentLengthHttpHeader;
import com.ramforth.webserver.http.headers.general.ContentDispositionHttpHeader;
import java.util.Map;
import java.util.TreeMap;

/**
 *
 * @author Tobias Ramforth <tobias.ramforth at tu-dortmund.de>
 */
public class HttpHeaderFactory implements IHttpHeaderFactory {

    private final Map<String, IHttpHeader> headerMap = new TreeMap<>();

    public HttpHeaderFactory() {
        headerMap.put("host", null);
        headerMap.put("http_x_requested_with", null);
        headerMap.put("content-length", null);
        headerMap.put("content-type", null);
        headerMap.put("transfer-encoding", null);
        headerMap.put("content-disposition", createContentDispositionHttpHeader());

    }

    @Override
    public IHttpHeader buildHttpHeader(String name, String rawValue) {
        IHttpHeader header;

        switch (name.toLowerCase()) {
            case "host":
                header = new HostHttpHeader(rawValue);
                break;
            case "http_x_requested_with":
                header = new AjaxHttpHeader();
                break;
            case "content-length":
                header = new ContentLengthHttpHeader(Long.parseLong(rawValue));
                break;
            case "content-type":
                header = new ContentTypeHttpHeader(rawValue);
                break;
            case "transfer-encoding":
                header = new TransferEncodingHttpHeader(rawValue);
                break;
            case "content-disposition":
                header = new ContentDispositionHttpHeader(rawValue);
                break;
            default:
                header = new StringHttpHeader(name, rawValue);
                break;
        }

        return header;
    }

    private IHttpHeader createContentDispositionHttpHeader() {
        return null;
    }
}
