/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package http.parsers;

import http.HttpRequestBodyData;
import java.io.InputStream;

/**
 *
 * @author Tobias Ramforth <tobias.ramforth at tu-dortmund.de>
 */
public interface IHttpRequestBodyParser {
  
  HttpRequestBodyData parse(InputStream inputStream);  
  
}