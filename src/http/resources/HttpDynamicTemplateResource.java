/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package http.resources;

import http.templates.WebFileTemplate;

/**
 *
 * @author Tobias Ramforth <tobias.ramforth at tu-dortmund.de>
 */
public class HttpDynamicTemplateResource extends AbstractHttpResource {
  
  private Class<? extends WebFileTemplate> templateType;
  
  public HttpDynamicTemplateResource(Class<? extends WebFileTemplate> templateType) {
    this.templateType = templateType;
  }
  
  public Class<? extends WebFileTemplate> getTemplateType() {
    return templateType;
  }
  
  public void setTemplateServerPath(Class<? extends WebFileTemplate> value) {
    this.templateType = value;
  }
  
}
