/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package web;

import java.util.TreeMap;

/**
 *
 * @author Tobias Ramforth <tobias.ramforth at tu-dortmund.de>
 */
public class Headers implements IHeaders {

  private TreeMap<String, IHeader> headers = new TreeMap<>();
  
  @Override
  public void addHeader(String name, IHeader value) {
    headers.put(name, value);
  }

  @Override
  public void removeHeader(String name) {
    headers.remove(name);
  }
  
  @Override
  public IHeader getHeader(String name) {
    return headers.get(name);
  }

  @Override
  public void clear() {
    headers.clear();
  }

  @Override
  public int numberOfHeaders() {
    return headers.size();
  }
  
}
