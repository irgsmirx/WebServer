/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package http.headers.general;

import http.headers.StringHttpHeader;

/**
 *
 * @author Tobias Ramforth <tobias.ramforth at tu-dortmund.de>
 */
public class TransferEncodingHttpHeader extends StringHttpHeader {
  
  public static final String TRANSFER_ENCODING = "Transfer-Encoding";
  
  public TransferEncodingHttpHeader(String rawValue) {
    super(TRANSFER_ENCODING, rawValue);
  }
  
}
