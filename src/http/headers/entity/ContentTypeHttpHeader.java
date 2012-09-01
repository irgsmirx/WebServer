/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package http.headers.entity;

import http.IMediaType;
import http.headers.StringHttpHeader;

/**
 *
 * @author Tobias Ramforth <tobias.ramforth at tu-dortmund.de>
 */
public class ContentTypeHttpHeader extends StringHttpHeader {
  
  public static final String CONTENT_TYPE = "Content-Type";

  private IMediaType mediaType;
  
  public ContentTypeHttpHeader(String rawValue) {
    super(CONTENT_TYPE, rawValue);
  }
  
}
