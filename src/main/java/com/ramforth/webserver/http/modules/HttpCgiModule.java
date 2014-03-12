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
package com.ramforth.webserver.http.modules;

import com.ramforth.webserver.WebServer;
import com.ramforth.webserver.http.HttpResponseWriter;
import com.ramforth.webserver.http.HttpStatusCode;
import com.ramforth.webserver.http.HttpStatusCodeClass;
import com.ramforth.webserver.http.IHttpContext;
import com.ramforth.webserver.http.IHttpHeader;
import com.ramforth.webserver.http.IHttpHeaders;
import com.ramforth.webserver.http.IHttpResponseWriter;
import com.ramforth.webserver.http.headers.entity.ContentLengthHttpHeader;
import com.ramforth.webserver.http.headers.entity.ContentTypeHttpHeader;
import com.ramforth.webserver.http.parsers.HttpHeadersParser;
import com.ramforth.webserver.http.resources.HttpFileResource;
import com.ramforth.webserver.http.resources.HttpFileResourceProvider;
import com.ramforth.webserver.web.ConnectionType;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

/**
 *
 * @author Tobias Ramforth
 */
public class HttpCgiModule extends AbstractHttpModule {

    public static final String CGI_VERSION = "CGI/1.1";
    
    private final String pathToCgiExecutable;
    private final String[] extensions;
    private final Pattern extensionsPattern;
    
    public HttpCgiModule(String pathToCgiExecutable, String... extensions) {
        super();
        this.resourceProvider = new HttpFileResourceProvider();

        this.pathToCgiExecutable = pathToCgiExecutable;
        this.extensions = extensions;
        this.extensionsPattern = constructRegexPatternFromExtensions(extensions);
    }
    
    private Pattern constructRegexPatternFromExtensions(String... extensions) {
        if (extensions.length == 0) {
            return Pattern.compile("[^.]*");
        }
        
        StringBuilder patternBuilder = new StringBuilder();
        
        patternBuilder.append("^.+")
                      .append("(");
        
        boolean pastFirst = false;
        for (String extension : extensions) {
            if (pastFirst) {
                patternBuilder.append("|");
                pastFirst = true;
            }

            extension = extension.trim();
            if (extension.startsWith(".")) {
                extension = extension.substring(1);
            }
            
            patternBuilder.append("\\.")
                          .append(extension);
        }
        
        
        patternBuilder.append(")")
                      .append("$");
        
        return Pattern.compile(patternBuilder.toString());
    }
    
    @Override
    public boolean processHttpContext(IHttpContext httpContext) {
        String requestedPath = httpContext.getRequest().getUri().getPath();
        
        if (!extensionsPattern.matcher(requestedPath).matches()) {
            return false;
        }

        if (!resourceExists(requestedPath)) {
            return false;
        }
        
        ProcessBuilder cgiProcessBuilder = new ProcessBuilder(pathToCgiExecutable, ((HttpFileResource)getResource(requestedPath)).getServerPath());
        String queryString = httpContext.getRequest().getUri().getQuery();
        if (queryString != null) {
            cgiProcessBuilder.environment().put("QUERY_STRING", queryString);
        }

        IHttpHeader requestHeader = httpContext.getRequest().getHeaders().getHeader("Content-Length");
        if (requestHeader != null) {
            ContentLengthHttpHeader contentLengthRequestHeader = (ContentLengthHttpHeader)requestHeader;
            cgiProcessBuilder.environment().put("CONTENT_LENGTH", String.valueOf(contentLengthRequestHeader.getValue()));
        }
        
        requestHeader = httpContext.getRequest().getHeaders().getHeader("Content-Type");
        if (requestHeader != null) {
            ContentTypeHttpHeader contentTypeRequestHeader = (ContentTypeHttpHeader)requestHeader;
            cgiProcessBuilder.environment().put("CONTENT_TYPE", String.valueOf(contentTypeRequestHeader.getValue()));
        }
        
        cgiProcessBuilder.environment().put("GATEWAY_INTERFACE", HttpCgiModule.CGI_VERSION);
        cgiProcessBuilder.environment().put("REMOTE_ADDR", httpContext.getRequest().getClientHostAddress());
        cgiProcessBuilder.environment().put("REMOTE_HOST", httpContext.getRequest().getClientHostName());
        cgiProcessBuilder.environment().put("REMOTE_USER", "NULL");
        cgiProcessBuilder.environment().put("REQUEST_METHOD", httpContext.getRequest().getMethod().toString());
        cgiProcessBuilder.environment().put("SERVER_NAME", httpContext.getRequest().getUri().getHost());
        cgiProcessBuilder.environment().put("SERVER_PORT", String.valueOf(httpContext.getRequest().getUri().getPort()));
        cgiProcessBuilder.environment().put("SERVER_PROTOCOL", httpContext.getRequest().getUri().getScheme());
        cgiProcessBuilder.environment().put("SERVER_SOFTWARE", String.format("%s/%s", WebServer.NAME, WebServer.VERSION.toString()));
        
        for (IHttpHeader header : httpContext.getRequest().getHeaders()) {
            cgiProcessBuilder.environment().put(String.format("HTTP_%s", header.getName().toUpperCase().replaceAll("-", "_")), header.getRawValue());
        }

//TODO:                
//      AUTH_TYPE
//      PATH_INFO
//      PATH_TRANSLATED
//      REMOTE_IDENT
//      REMOTE_USER
//      SCRIPT_NAME
        
        Process cgiProcess;
        try {
            cgiProcess = cgiProcessBuilder.start();
        }
        catch (IOException ex) {
            Logger.getLogger(HttpCgiModule.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }

        OutputStream cgiOutputStream = cgiProcess.getOutputStream();
        
        for (IHttpHeader header : httpContext.getRequest().getHeaders()) {
            try {
                cgiOutputStream.write(header.toString().getBytes(Charset.defaultCharset()));
            }
            catch (IOException ex) {
                Logger.getLogger(HttpCgiModule.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        httpContext.getResponse().setStatusCode(new HttpStatusCode(HttpStatusCodeClass.SUCCESS, 200, ""));
        httpContext.getResponse().setConnectionType(ConnectionType.CLOSE);
        
        HttpHeadersParser httpHeadersParser = new HttpHeadersParser();
        IHttpHeaders responseHeaders = httpHeadersParser.parse(cgiProcess.getInputStream());
        for (IHttpHeader responseHeader : responseHeaders) {
            httpContext.getResponse().getHeaders().addHeader(responseHeader);
        }

        IHttpResponseWriter httpResponseWriter = new HttpResponseWriter(httpContext.getResponse().getOutputStream());
        httpResponseWriter.writeResponse(httpContext.getResponse());
        
        BufferedInputStream bis = new BufferedInputStream(cgiProcess.getInputStream());
        byte[] buffer = new byte[4096];
        int bytesRead = -1;
        try {
            while ((bytesRead = bis.read(buffer)) != -1) {
                httpContext.getResponse().getOutputStream().write(buffer, 0, bytesRead);
            }
        }
        catch (IOException ioex) {
            Logger.getLogger(HttpCgiModule.class.getName()).log(Level.SEVERE, null, ioex);
            throw new com.ramforth.webserver.exceptions.IOException(ioex);
        }
        
        return true;
    }
    
}
