/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ramforth.webserver.http.modules;

import com.ramforth.webserver.http.HttpStatusCode;
import com.ramforth.webserver.http.HttpStatusCodeClass;
import com.ramforth.webserver.http.IHttpContext;
import com.ramforth.webserver.http.IHttpHeader;
import com.ramforth.webserver.http.IHttpHeaders;
import com.ramforth.webserver.http.headers.entity.ContentLengthHttpHeader;
import com.ramforth.webserver.http.parsers.HttpHeadersParser;
import com.ramforth.webserver.http.resources.HttpFileResource;
import com.ramforth.webserver.http.resources.HttpFileResourceProvider;
import com.ramforth.webserver.web.ConnectionType;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.nio.charset.Charset;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

/**
 *
 * @author Tobias Ramforth
 */
public class HttpCgiModule extends AbstractHttpModule {

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
