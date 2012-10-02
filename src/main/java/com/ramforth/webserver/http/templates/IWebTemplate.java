/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ramforth.webserver.http.templates;

import com.ramforth.webserver.http.IHttpContext;

/**
 *
 * @author tobias
 */
public interface IWebTemplate {

    IHttpContext getContext();

    void setContext(IHttpContext context);

    void load();
    
    void post();
    
    void get();

    String getContentType();
}
