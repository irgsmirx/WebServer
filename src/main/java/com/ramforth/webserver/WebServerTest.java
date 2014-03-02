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
