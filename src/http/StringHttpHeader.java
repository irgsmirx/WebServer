/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package http;

/**
 *
 * @author Tobias Ramforth <tobias.ramforth at tu-dortmund.de>
 */
public class StringHttpHeader extends HttpHeader {
  
  public StringHttpHeader(String name, String rawValue) {
    super(name, rawValue);
  }
  
  public String getValue() {
    return rawValue;
  }
  
  public void setValue(String value) {
    this.rawValue = value;
  }
  
}
