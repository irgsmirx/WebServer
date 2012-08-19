/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package http;

/**
 *
 * @author Tobias Ramforth <tobias.ramforth at tu-dortmund.de>
 */
public class AjaxHttpHeader extends StringHttpHeader {
  
  public AjaxHttpHeader() {
    super("HTTP_X_REQUESTED_WITH", "xmlhttprequest");
  }
  
}
