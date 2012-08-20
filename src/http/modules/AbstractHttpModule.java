/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package http.modules;

import http.resources.IHttpResourceProvider;

/**
 *
 * @author Tobias Ramforth <tobias.ramforth at tu-dortmund.de>
 */
public abstract class AbstractHttpModule implements IHttpModule {
  
  protected boolean handled;
  protected IHttpResourceProvider resourceProvider;
  
	@Override
  public boolean isHandled() {
    return handled;
  }
	
}
