/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package http;

/**
 *
 * @author Tobias Ramforth <tobias.ramforth at tu-dortmund.de>
 */
public interface IHttpHeaderList {
  
  void addHeader(IHttpHeader value);
  void removeHeader(IHttpHeader value);
  void clear();
  int numberOfHeaders();
  
}
