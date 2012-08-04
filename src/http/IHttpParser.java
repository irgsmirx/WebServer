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
  
  IHttpHeader parseHeader(InputStream is);
  
  IHttpRequestLine parseRequestLine(InputStream is) throws IOException, HttpException;
  
}
