/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package http;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Tobias Ramforth <tobias.ramforth at tu-dortmund.de>
 */
public class HttpHeaderList implements IHttpHeaderList {

  private List<IHttpHeader> headers = new ArrayList<>();
  
  @Override
  public void addHeader(IHttpHeader value) {
    headers.add(value);
  }

  @Override
  public void removeHeader(IHttpHeader value) {
    headers.remove(value);
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
