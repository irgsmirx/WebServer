/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package http;

import java.io.InputStream;

/**
 *
 * @author Tobias Ramforth <tobias.ramforth at tu-dortmund.de>
 */
public interface IHttpParser {
  
  IHttpRequest parseRequest(InputStream is);
  
  IHttpHeaders parseHeaders(InputStream is);
  
  IHttpRequestLine parseRequestLine(InputStream is);

  IHttpHeader parseHeader(InputStream is);
    
  byte[] parseBody(InputStream is, IHttpHeaders headers);
  
}
