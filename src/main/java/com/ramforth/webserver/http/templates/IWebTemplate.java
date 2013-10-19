/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ramforth.webserver.http.templates;

import com.ramforth.webserver.http.IHttpContext;
import java.nio.charset.Charset;

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
    
    Charset getCharset();
    
    void setCharset(Charset charset);
    
}
