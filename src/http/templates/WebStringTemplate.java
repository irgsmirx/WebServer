/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package http.templates;

import http.IHttpContext;
import utilities.templates.StringTemplate;

/**
 *
 * @author Tobias Ramforth <tobias.ramforth at tu-dortmund.de>
 */
public class WebStringTemplate extends StringTemplate implements IWebTemplate {
  
  protected IHttpContext context;
  
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

}
