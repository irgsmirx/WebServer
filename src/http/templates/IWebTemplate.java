/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package http.templates;

import http.IHttpContext;

/**
 *
 * @author tobias
 */
public interface IWebTemplate {
	
	IHttpContext getContext();

  void setContext(IHttpContext context);

  void load();
  
  String getContentType();
	
}
