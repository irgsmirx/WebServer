/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package http.headers;

import http.HttpHeader;

/**
 *
 * @author Tobias Ramforth <tobias.ramforth at tu-dortmund.de>
 */
public class LongHttpHeader extends HttpHeader {
  
  private long value;
  
  public LongHttpHeader(String name, String rawValue) {
    super(name, rawValue);
    this.value = Long.parseLong(rawValue);
  }

  public LongHttpHeader(String name, long value) {
    super(name, String.valueOf(value));
    this.value = value;
  }
  
  public long getValue() {
    return value;
  }
  
  public void setValue(long value) {
    this.value = value;
    this.rawValue = String.valueOf(value);
  }
  
}
