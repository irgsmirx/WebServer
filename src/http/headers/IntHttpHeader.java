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
public class IntHttpHeader extends HttpHeader {
  
  private int value;
  
  public IntHttpHeader(String name, String rawValue) {
    super(name, rawValue);
    this.value = Integer.parseInt(rawValue);
  }
  
  public int getValue() {
    return value;
  }
  
  public void setValue(int value) {
    this.value = value;
    this.rawValue = String.valueOf(value);
  }
  
}
