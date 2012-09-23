/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ramforth.webserver;

import com.ramforth.webserver.http.HttpListener;
import com.ramforth.webserver.http.IHttpContext;
import com.ramforth.webserver.http.IHttpListener;
import com.ramforth.webserver.http.modules.HttpFileModule;
import com.ramforth.webserver.http.modules.IHttpModule;
import com.ramforth.webserver.http.resources.HttpFileResource;

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
    
    webServer.addHttpListener(httpListener);
    
    webServer.start();
  }
  
  public static class Bla implements IHttpModule {

    @Override
    public boolean processHttpContext(IHttpContext context) {
      System.out.println(context.getRequest().getMethod());
      System.out.println(context.getRequest().getUri());
      System.out.println(context.getRequest().getVersion());
      return true;
    }
        
  }
}
