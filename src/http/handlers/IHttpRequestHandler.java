/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package http.handlers;

import http.*;

/**
 *
 * @author Tobias Ramforth <tobias.ramforth at tu-dortmund.de>
 */
public interface IHttpRequestHandler {
  
  void handleRequest(IHttpRequest request);
  
}
