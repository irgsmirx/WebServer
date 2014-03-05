/* 
 * Copyright (C) 2014 Tobias Ramforth
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.ramforth.webserver.examples;

import com.ramforth.webserver.WebServer;
import com.ramforth.webserver.http.HttpListener;
import com.ramforth.webserver.http.IHttpListener;
import com.ramforth.webserver.http.modules.HttpFileModule;
import com.ramforth.webserver.http.resources.HttpFileResource;

/**
 *
 * @author Tobias Ramforth <tobias.ramforth at tu-dortmund.de>
 */
public class WebServerFileExample {

    public WebServerFileExample() {
        // instantiate a new web server
        WebServer webServer = new WebServer();

        // add a file module serving only static files
        HttpFileModule httpFileModule = new HttpFileModule();
        
        // instantiate a single static file resource
        HttpFileResource staticFile = new HttpFileResource();
        // set the path we can use to open the file e.g. with a web browser
        staticFile.setRelativePath("/index.htm");
        // set the path where the file is located on disk
        staticFile.setServerPath(WebServerFileExample.class.getResource("/examples/staticFileTest.htm").getPath());
        // add the newly created resource to the module
        httpFileModule.getResources().addResource(staticFile);
        
        // add the module to the server
        webServer.addModule(httpFileModule);

        // create an http listener for InetAddress.getLocalhost() on port 11111
        IHttpListener httpListener = new HttpListener(11111);
        
        // add the listener to the server
        webServer.addHttpListener(httpListener);
        
        // start the web server
        webServer.start();
    }

    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        WebServerFileExample fileExample = new WebServerFileExample();
    }

}
