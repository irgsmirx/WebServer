/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ramforth.webserver.web;

import java.io.InputStream;

/**
 *
 * @author Tobias Ramforth <tobias.ramforth at tu-dortmund.de>
 */
public class Request extends Message implements IRequest {
  
  private InputStream inputStream;
  
  @Override
  public InputStream getInputStream() {
    return inputStream;
  }
  
  @Override
  public void setInputStream(InputStream value) {
    this.inputStream = value;
  }
  
}
