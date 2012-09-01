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
public class HttpRequestTextXmlBodyParser implements IHttpRequestBodyParser {

  @Override
  public HttpRequestBodyData parse(InputStream inputStream) {
    throw new UnsupportedOperationException("Not supported yet.");
  }
  
}
