/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package http;

/**
 *
 * @author Tobias Ramforth <tobias.ramforth at tu-dortmund.de>
 */
public class TransferEncodingHttpHeader extends StringHttpHeader {
  
  public TransferEncodingHttpHeader(String rawValue) {
    super("Transfer-Encoding", rawValue);
  }
  
}
