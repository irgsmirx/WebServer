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
  
  HttpRequest getRequest();
  
  HttpResponse getResponse();
  
  HttpSessionState getSession();
  
}