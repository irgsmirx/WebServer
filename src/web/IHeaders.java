/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package web;

/**
 *
 * @author Tobias Ramforth <tobias.ramforth at tu-dortmund.de>
 */
public interface IHeaders {
 
  void addHeader(String name, IHeader value);
  void removeHeader(String name);
  IHeader getHeader(String name);
  void clear();
  int numberOfHeaders();
  
}
