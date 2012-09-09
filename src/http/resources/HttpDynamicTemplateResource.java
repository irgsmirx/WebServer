/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package http.resources;

import http.templates.IWebTemplate;

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
  
}
