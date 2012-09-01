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
public class ContentMd5HttpHeader extends LongHttpHeader {

  public static final String CONTENT_MD5 = "Content-MD5";

  public ContentMd5HttpHeader(long value) {
    super(CONTENT_MD5, value);
  }
  
}
