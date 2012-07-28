/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package http;

/**
 *
 * @author Tobias Ramforth <tobias.ramforth at tu-dortmund.de>
 */
public class HttpContext {
  
  private HttpRequest request;
  private HttpResponse response;
  private HttpSessionState session;
  
  public HttpContext(HttpRequest request, HttpResponse response) {
    this.request = request;
    this.response = response;
  }
  
  public HttpRequest getRequest() {
    return request;
  }
  
  public HttpResponse getResponse() {
    return response;
  }
  
  public HttpSessionState getSession() {
    return session;
  }
  
}
