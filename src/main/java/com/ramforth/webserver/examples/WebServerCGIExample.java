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
import com.ramforth.webserver.http.modules.HttpCgiModule;
import com.ramforth.webserver.http.resources.HttpFileResource;

/**
 *
 * @author Tobias Ramforth <tobias.ramforth at tu-dortmund.de>
 */
public class WebServerCGIExample {

    public WebServerCGIExample() {
        // instantiate a new web server
        WebServer webServer = new WebServer();

        // add a cgi module using perl as backend for files ending with .pl (adjust path to perl executable as needed)
        HttpCgiModule httpCgiModule = new HttpCgiModule("/usr/bin/perl", ".pl");
        
        // instantiate a single file resource
        HttpFileResource perlFile = new HttpFileResource();
        // set the path we can use to open the file e.g. with a web browser
        perlFile.setRelativePath("/index.pl");
        // set the path where the file is located on disk
        perlFile.setServerPath(WebServerCGIExample.class.getResource("/examples/perlCgiTest.pl").getPath());
        // add the newly created resource to the module
        httpCgiModule.getResources().addResource(perlFile);
        
        // add the module to the server
        webServer.addModule(httpCgiModule);

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
        WebServerCGIExample cgiExample = new WebServerCGIExample();
    }

}
