/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ramforth.webserver.http;

/**
 *
 * @author Tobias Ramforth <tobias.ramforth at tu-dortmund.de>
 */
public class MediaType implements IMediaType {
  
  protected String type;
  protected NameValueMap parameters = new NameValueMap();

  @Override
  public String getType() {
    return type;
  }

  @Override
  public void setType(String value) {
    this.type = value;
  }

  @Override
  public NameValueMap getParameters() {
    return parameters;
  }

  @Override
  public void addParameter(String name, String value) {
    parameters.add(name, value);
  }
  
  @Override
  public void removeParameter(String name) {
    parameters.remove(name);
  }
  
  @Override
  public void clearParameters() {
    parameters.clear();
  }

  @Override
  public int numberOfParameters() {
    return parameters.numberOfEntries();
  }
  
}
