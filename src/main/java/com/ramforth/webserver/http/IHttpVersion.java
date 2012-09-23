/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ramforth.webserver.http;

/**
 *
 * @author Tobias Ramforth <tobias.ramforth at tu-dortmund.de>
 */
public interface IHttpVersion {
  
  int getMinor();
  void setMinor(int value);
  
  int getMajor();
  void setMajor(int value);
  
  boolean isHTTP10();
  boolean isHTTP11();
  
}
