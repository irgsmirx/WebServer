/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package http.modules;

import http.IHttpContext;
import web.modules.IModule;

/**
 *
 * @author Tobias Ramforth <tobias.ramforth at tu-dortmund.de>
 */
public interface IHttpModule extends IModule {
  
  boolean processHttpContext(IHttpContext httpContext);
  
}
