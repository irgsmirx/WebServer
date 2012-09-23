/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ramforth.webserver.web;

import java.io.OutputStream;

/**
 *
 * @author Tobias Ramforth <tobias.ramforth at tu-dortmund.de>
 */
public class Response extends Message implements IResponse {

  protected OutputStream outputStream;
  protected ConnectionType connectionType;
  
  @Override
  public ConnectionType getConnectionType() {
    return connectionType;
  }

  @Override
  public void setConnectionType(ConnectionType value) {
    this.connectionType = value;
  }
  
  @Override
  public OutputStream getOutputStream() {
    return outputStream;
  }
  
  @Override
  public void setOutputStream(OutputStream value) {
    this.outputStream = value;
  }
  
}
