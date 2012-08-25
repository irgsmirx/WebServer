/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package http.modules;

import http.resources.IHttpResource;
import http.resources.IHttpResourceProvider;

/**
 *
 * @author Tobias Ramforth <tobias.ramforth at tu-dortmund.de>
 */
public abstract class AbstractHttpModule implements IHttpModule {
  
  protected IHttpResourceProvider resourceProvider;
 
  public IHttpResourceProvider getResources() {
    return resourceProvider;
  }
  
  protected boolean resourceExists(String uriPath) {
    return resourceProvider.containsResource(uriPath);
  }
  
  protected IHttpResource getResource(String uriPath) {
    return resourceProvider.getResource(uriPath);
  }
	
}
