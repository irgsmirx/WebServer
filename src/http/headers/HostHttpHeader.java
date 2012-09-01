/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package http.headers;

/**
 *
 * @author Tobias Ramforth <tobias.ramforth at tu-dortmund.de>
 */
public class HostHttpHeader extends StringHttpHeader {
  
  public HostHttpHeader(String hostname) {
    super("Host", hostname);
  }
  
}
