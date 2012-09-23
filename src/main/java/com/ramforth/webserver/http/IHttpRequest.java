/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ramforth.webserver.http;

import com.ramforth.webserver.web.IRequest;
import java.net.URI;

/**
 *
 * @author Tobias Ramforth <tobias.ramforth at tu-dortmund.de>
 */
public interface IHttpRequest extends IHttpMessage, IRequest {

    HttpMethod getMethod();

    void setMethod(HttpMethod value);

    URI getUri();

    void setUri(URI value);

    public NameValueMap getQueryString();

    public String getQueryString(String key);

    public void setQueryString(String key, String value);

    public NameValueMap getForm();

    public String getForm(String key);

    public void setForm(String key, String value);

    public NameValueMap getParams();
}
