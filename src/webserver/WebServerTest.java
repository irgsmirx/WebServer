/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package webserver;

import http.HttpListener;
import http.IHttpContext;
import http.IHttpContextHandler;
import http.IHttpListener;

/**
 *
 * @author Tobias Ramforth <tobias.ramforth at tu-dortmund.de>
 */
public class WebServerTest {

  /**
   * @param args the command line arguments
   */
  public static void main(String[] args) {
    WebServer webServer = new WebServer();
    
    IHttpListener httpListener = new HttpListener(11111);
    webServer.addContextHandler(new Bla());
    
    webServer.httpListeners.add(httpListener);
    
    webServer.start();
  }
  
  public static class Bla implements IHttpContextHandler {

    @Override
    public void handleContext(IHttpContext context) {
      System.out.println(context.getRequest().getMethod());
      System.out.println(context.getRequest().getUri());
      System.out.println(context.getRequest().getVersion());
    }
        
  }
}
