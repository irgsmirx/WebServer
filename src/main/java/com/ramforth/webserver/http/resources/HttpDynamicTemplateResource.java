/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ramforth.webserver.http.resources;

import com.ramforth.webserver.http.templates.IWebTemplate;
import java.io.InputStream;

/**
 *
 * @author Tobias Ramforth <tobias.ramforth at tu-dortmund.de>
 */
public class HttpDynamicTemplateResource extends AbstractHttpResource {

    private Class<? extends IWebTemplate> templateType;

    public HttpDynamicTemplateResource(Class<? extends IWebTemplate> templateType) {
        this.templateType = templateType;
    }

    public Class<? extends IWebTemplate> getTemplateType() {
        return templateType;
    }

    public void setTemplateServerPath(Class<? extends IWebTemplate> value) {
        this.templateType = value;
    }

    @Override
    public InputStream tryOpenStream() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
}
