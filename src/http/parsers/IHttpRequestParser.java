/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package http.parsers;

import http.IHttpHeader;
import http.IHttpHeaders;
import http.IHttpRequest;
import http.IHttpRequestLine;
import java.io.InputStream;

/**
 *
 * @author Tobias Ramforth <tobias.ramforth at tu-dortmund.de>
 */
public interface IHttpRequestParser {
  
  IHttpRequest parseRequest(InputStream is);
  
  IHttpHeaders parseHeaders(InputStream is);
  
  IHttpRequestLine parseRequestLine(InputStream is);

  IHttpHeader parseHeader(InputStream is);
    
  byte[] parseBody(InputStream is, IHttpHeaders headers);
  
}
