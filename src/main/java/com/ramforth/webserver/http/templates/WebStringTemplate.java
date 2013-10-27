/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ramforth.webserver.http.templates;

import com.ramforth.utilities.templates.StringTemplate;
import com.ramforth.webserver.http.IHttpContext;

/**
 *
 * @author Tobias Ramforth <tobias.ramforth at tu-dortmund.de>
 */
public class WebStringTemplate extends StringTemplate implements IWebTemplate {

    private static final String DEFAULT_CONTENT_TYPE = "text/html";
    protected IHttpContext context;
    protected String contentType = DEFAULT_CONTENT_TYPE;

    public WebStringTemplate(String template) {
        super(template);
    }

    @Override
    public IHttpContext getContext() {
        return context;
    }

    @Override
    public void setContext(IHttpContext context) {
        this.context = context;
    }

    @Override
    public void load() {
    }
    
    @Override
    public void post() {
    }

    @Override
    public void get() {
    }

    @Override
    public final String getContentType() {
        return contentType;
    }
    
}
