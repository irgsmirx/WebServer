/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package http.headers.entity;

import http.headers.LongHttpHeader;

/**
 *
 * @author Tobias Ramforth <tobias.ramforth at tu-dortmund.de>
 */
public class ExpiresHttpHeader extends LongHttpHeader {
  
  public static final String EXPIRES = "Expires";
  
  public ExpiresHttpHeader(long value) {
    super(EXPIRES, value);
  }
  
}
