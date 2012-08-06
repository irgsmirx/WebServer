/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package http;

import web.Header;

/**
 *
 * @author Tobias Ramforth <tobias.ramforth at tu-dortmund.de>
 */
public class HttpHeader extends Header implements IHttpHeader {

  private String name;
  protected String rawValue;
  
  public HttpHeader(String name, String rawValue) {
    this.name = name;
    this.rawValue = rawValue;
  }
  
  @Override
  public String getName() {
    return name;
  }

  @Override
  public void setName(String value) {
    this.name = value;
  }

  @Override
  public String getRawValue() {
    return rawValue;
  }

  @Override
  public void setRawValue(String value) {
    this.rawValue = value;
  }
  
}
