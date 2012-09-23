/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ramforth.webserver.http.handlers;

import com.ramforth.webserver.http.IHttpRequest;

/**
 *
 * @author Tobias Ramforth <tobias.ramforth at tu-dortmund.de>
 */
public interface IHttpRequestHandler {
  
  void handleRequest(IHttpRequest request);
  
}
