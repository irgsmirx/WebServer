/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package http;

/**
 *
 * @author Tobias Ramforth <tobias.ramforth at tu-dortmund.de>
 */
public class HttpRequestLine implements IHttpRequestLine {

  private HttpMethod method;
  private IHttpVersion version;
  
  public HttpRequestLine(HttpMethod method, IHttpVersion version) {
    this.method = method;
    this.version = version;
  }
  
  @Override
  public HttpMethod getMethod() {
    return method;
  }

  @Override
  public void setMethod(HttpMethod value) {
    this.method = value;
  }

  @Override
  public IHttpVersion getVersion() {
    return version;
  }

  @Override
  public void setVersion(IHttpVersion value) {
    this.version = value;
  }
  
}
