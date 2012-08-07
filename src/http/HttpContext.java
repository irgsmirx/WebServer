/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package http;

/**
 *
 * @author Tobias Ramforth <tobias.ramforth at tu-dortmund.de>
 */
public class HttpContext implements IHttpContext {
  
  private IHttpRequest request;
  private IHttpResponse response;
  private HttpSessionState session;
  
  public HttpContext(IHttpRequest request, IHttpResponse response) {
    this.request = request;
    this.response = response;
  }
  
  @Override
  public IHttpRequest getRequest() {
    return request;
  }
  
  @Override
  public IHttpResponse getResponse() {
    return response;
  }
  
  @Override
  public HttpSessionState getSession() {
    return session;
  }
  
}
