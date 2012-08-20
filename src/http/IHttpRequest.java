/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package http;

import java.net.URI;
import web.IRequest;

/**
 *
 * @author Tobias Ramforth <tobias.ramforth at tu-dortmund.de>
 */
public interface IHttpRequest extends IHttpMessage, IRequest {
 
  HttpMethod getMethod();
  void setMethod(HttpMethod value);
	
  URI getUri();
  void setUri(URI value);
  
}
