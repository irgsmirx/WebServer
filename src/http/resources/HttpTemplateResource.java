/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package http.resources;

import utilities.templates.ITemplate;

/**
 *
 * @author Tobias Ramforth <tobias.ramforth at tu-dortmund.de>
 */
public class HttpTemplateResource extends HttpFileResource {
  
  private ITemplate template;
  
  public HttpTemplateResource(ITemplate template) {
    this.template = template;
  }
  
  public ITemplate getTemplate() {
    return template;
  }
  
  public void setTemplate(ITemplate template) {
    this.template = template;
  }
  
}
