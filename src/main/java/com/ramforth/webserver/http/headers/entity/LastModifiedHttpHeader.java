/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ramforth.webserver.http.headers.entity;

import com.ramforth.webserver.http.headers.LongHttpHeader;

/**
 *
 * @author Tobias Ramforth <tobias.ramforth at tu-dortmund.de>
 */
public class LastModifiedHttpHeader extends LongHttpHeader {
  
  public static final String LAST_MODIFIED = "Last-Modified";
  
  public LastModifiedHttpHeader(long value) {
    super(LAST_MODIFIED, value);
  }
  
}
