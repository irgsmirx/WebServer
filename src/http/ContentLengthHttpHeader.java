/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package http;

/**
 *
 * @author Tobias Ramforth <tobias.ramforth at tu-dortmund.de>
 */
public class ContentLengthHttpHeader extends LongHttpHeader {
  
  public ContentLengthHttpHeader(long value) {
    super("Content-Length", value);
  }
  
}
