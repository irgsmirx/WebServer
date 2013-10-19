/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ramforth.webserver.http.templates;

import com.ramforth.webserver.http.IHttpContext;
import java.nio.charset.Charset;

/**
 *
 * @author Tobias Ramforth <tobias.ramforth at tu-dortmund.de>
 */
public abstract class AbstractWebTemplate implements IWebTemplate {
    
    protected Charset charset = Charset.defaultCharset();
    protected IHttpContext context;
    
    @Override
    public IHttpContext getContext() {
        return context;
    }

    @Override
    public void setContext(IHttpContext context) {
        this.context = context;
    }

    @Override
    public Charset getCharset() {
        return charset;
    }

    @Override
    public void setCharset(Charset charset) {
        this.charset = charset;
    }
    
}
