/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ramforth.webserver.http;

import com.ramforth.utilities.common.implementation.Pair;
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

    NameValueMap getQueryString();

    String getQueryString(String key);

    void setQueryString(String key, String value);

    NameValueMap getForm();

    String getForm(String key);

    void setForm(String key, String value);

    NameValueMap getParams();

    String getParams(String key);

    Iterable<HttpPostedFile> getPostedFiles();

    HttpPostedFile getPostedFile(String name);

    int getNumberOfPostedFiles();

}
