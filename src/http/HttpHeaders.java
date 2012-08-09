/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package http;

import java.util.Iterator;
import java.util.TreeMap;

/**
 *
 * @author Tobias Ramforth <tobias.ramforth at tu-dortmund.de>
 */
public class HttpHeaders implements IHttpHeaders {

  private TreeMap<String, IHttpHeader> headers = new TreeMap<>(); 
  
  @Override
  public void addHeader(IHttpHeader value) {
    headers.put(value.getName(), value);
  }

  @Override
  public void removeHeader(String name) {
    headers.remove(name);
  }

  @Override
  public IHttpHeader getHeader(String name) {
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

  @Override
  public Iterator<IHttpHeader> iterator() {
    return headers.values().iterator();
  }
  
}
