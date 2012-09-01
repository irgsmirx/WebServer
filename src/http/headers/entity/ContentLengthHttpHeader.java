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
public class ContentLengthHttpHeader extends LongHttpHeader {
  
  public static final String CONTENT_LENGTH = "Content-Length";

  public ContentLengthHttpHeader(long value) {
    super(CONTENT_LENGTH, value);
  }
  
}
