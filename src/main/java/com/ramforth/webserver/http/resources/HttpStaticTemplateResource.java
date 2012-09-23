/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ramforth.webserver.http.resources;

import com.ramforth.utilities.templates.ITemplate;

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
}
