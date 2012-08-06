/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package http;

import exceptions.HttpException;
import java.io.IOException;
import java.io.InputStream;

/**
 *
 * @author Tobias Ramforth <tobias.ramforth at tu-dortmund.de>
 */
public interface IHttpParser {
  
  IHttpHeaders parseHeaders(InputStream is) throws IOException, HttpException;
  
  IHttpRequestLine parseRequestLine(InputStream is) throws IOException, HttpException;

  IHttpHeader parseHeader(InputStream is);
    
  void parseBody(InputStream is, IHttpHeaders headers) throws IOException, HttpException;
  
}
