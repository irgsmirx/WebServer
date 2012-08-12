/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package webserver;

import http.HttpListener;
import http.IHttpContext;
import http.IHttpListener;
import http.modules.HttpFileModule;
import http.modules.IHttpModule;
import http.resources.HttpFileResource;

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
    
    HttpFileModule httpFileModule = new HttpFileModule();
    HttpFileResource hfr = new HttpFileResource();
    hfr.setRelativePath("/index.htm");
    hfr.setServerPath("/home/tobias/test.htm");
    httpFileModule.getResources().addResource(hfr);
    webServer.addModule(httpFileModule);
    
    webServer.httpListeners.add(httpListener);
    
    webServer.start();
  }
  
  public static class Bla implements IHttpModule {

    @Override
    public void processHttpContext(IHttpContext context) {
      System.out.println(context.getRequest().getMethod());
      System.out.println(context.getRequest().getUri());
      System.out.println(context.getRequest().getVersion());
    }

    @Override
    public boolean isHandled() {
      return false;
    }
        
  }
}
