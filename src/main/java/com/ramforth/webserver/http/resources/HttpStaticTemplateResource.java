/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ramforth.webserver.http.resources;

import com.ramforth.utilities.templates.ITemplate;
import java.io.InputStream;

/**
 *
 * @author Tobias Ramforth <tobias.ramforth at tu-dortmund.de>
 */
public class HttpStaticTemplateResource extends AbstractHttpResource {

    private ITemplate template;

    public HttpStaticTemplateResource(ITemplate template) {
        this.template = template;
    }

    public ITemplate getTemplate() {
        return template;
    }

    public void setTemplate(ITemplate template) {
        this.template = template;
    }

    @Override
    public InputStream tryOpenStream() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
