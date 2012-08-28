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
  
	public NameValueMap getQueryString();
  public String getQueryString(String key);
  public void setQueryString(String key, String value);

  public NameValueMap getForm();
  public String getForm(String key);
  public void setForm(String key, String value);
  
	public NameValueMap getParams();
  
}
