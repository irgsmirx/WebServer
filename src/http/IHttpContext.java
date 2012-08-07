/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package http;

/**
 *
 * @author Tobias Ramforth <tobias.ramforth at tu-dortmund.de>
 */
public interface IHttpContext {
  
  IHttpRequest getRequest();
  
  IHttpResponse getResponse();
  
  HttpSessionState getSession();
  
}
