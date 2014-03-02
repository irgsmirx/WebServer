/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ramforth.webserver.http.templates;

import com.ramforth.webserver.http.IHttpContext;

/**
 *
 * @author Tobias Ramforth <tobias.ramforth at tu-dortmund.de>
 */
public abstract class AbstractWebTemplate implements IWebTemplate {

    protected IHttpContext context;

    @Override
    public IHttpContext getContext() {
        return context;
    }

    @Override
    public void setContext(IHttpContext context) {
        this.context = context;
    }

}
