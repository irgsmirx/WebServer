/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package http;

import web.IHeader;

/**
 *
 * @author Tobias Ramforth <tobias.ramforth at tu-dortmund.de>
 */
public interface IHttpHeaders {
  
  void addHeader(IHttpHeader value);
  void removeHeader(String name);
  IHeader getHeader(String name);
  void clear();
  int numberOfHeaders();
  
}
