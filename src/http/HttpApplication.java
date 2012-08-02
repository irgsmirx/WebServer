/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package http;

/**
 *
 * @author Tobias Ramforth <tobias.ramforth at tu-dortmund.de>
 */
public class HttpApplication {
  
  private HttpContext context;
  private HttpSessionState session;
  
  
  public HttpRequest getRequest() {
    HttpRequest httpRequest = null;
    
    if (context != null) {
      httpRequest = context.getRequest();
    }
    
    if (httpRequest == null) {
      throw new HttpException("Request not available");
    }
    
    return httpRequest;
  }

  public HttpResponse getResponse() {
    HttpResponse httpResponse = null;
    
    if (context != null) {
      httpResponse = context.getResponse();
    }
    
    if (httpResponse == null) {
      throw new HttpException("Response not available");
    }
    
    return httpResponse;
  }
    
  public HttpSessionState getSession() {
    HttpSessionState httpSessionState = null;
    
    if (session != null) {
      httpSessionState = session;
    } else {
      if (context != null) {
        httpSessionState = context.getSession();
      }
    }
    
    if (httpSessionState == null) {
      throw new HttpException("Session not available");
    }
    
    return httpSessionState;
  }
  
}