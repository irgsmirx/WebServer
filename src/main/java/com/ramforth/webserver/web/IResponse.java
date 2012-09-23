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
public interface IResponse {
    
  ConnectionType getConnectionType();
  void setConnectionType(ConnectionType value);
  
  OutputStream getOutputStream();
  void setOutputStream(OutputStream value);
  
}
  
