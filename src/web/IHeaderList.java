/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package web;

/**
 *
 * @author Tobias Ramforth <tobias.ramforth at tu-dortmund.de>
 */
public interface IHeaderList {
 
  void addHeader(IHeader value);
  void removeHeader(IHeader value);
  void clear();
  
}
