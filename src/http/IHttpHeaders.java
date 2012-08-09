/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package http;

/**
 *
 * @author Tobias Ramforth <tobias.ramforth at tu-dortmund.de>
 */
public interface IHttpHeaders extends Iterable<IHttpHeader> {
  
  void addHeader(IHttpHeader value);
  void removeHeader(String name);
  IHttpHeader getHeader(String name);
  boolean contains(String name);
  void clear();
  int numberOfHeaders();
  
}
