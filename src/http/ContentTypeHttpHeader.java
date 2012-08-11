/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package http;

/**
 *
 * @author Tobias Ramforth <tobias.ramforth at tu-dortmund.de>
 */
public class ContentTypeHttpHeader extends StringHttpHeader {
  
  public ContentTypeHttpHeader(String name, String rawValue) {
    super("Content-Type", rawValue);
  }
  
}
