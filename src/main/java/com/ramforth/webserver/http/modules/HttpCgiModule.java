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

    private final String pathToCgiExecutable;
    private final String[] extensions;
    private final Pattern extensionsPattern;
    
    public HttpCgiModule(String pathToCgiExecutable, String... extensions) {
        this.pathToCgiExecutable = pathToCgiExecutable;
        this.extensions = extensions;
        this.extensionsPattern = constructRegexPatternFromExtensions(extensions);
    }
    
    private Pattern constructRegexPatternFromExtensions(String... extensions) {
        if (extensions.length == 0) {
            return Pattern.compile("[^.]*");
        }
        
        StringBuilder patternBuilder = new StringBuilder();
        
        patternBuilder.append("(");
        
        for (String extension : extensions) {
            if (patternBuilder.length() > 0) {
                patternBuilder.append("|");
            }

                extension = extension.trim();
            if (extension.startsWith(".")) {
                extension = extension.substring(1);
            }
            
            patternBuilder.append("\\.")
                          .append(extension);
        }
        
        
        patternBuilder.append(")")
                      .append("\\z");
        
        return Pattern.compile(patternBuilder.toString());
    }
    
    @Override
    public boolean processHttpContext(IHttpContext httpContext) {
        if (!extensionsPattern.matcher(httpContext.getRequest().getUri().getPath()).matches()) {
            return false;
        }
        
        ProcessBuilder cgiProcessBuilder = new ProcessBuilder(pathToCgiExecutable);
        
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
        
        BufferedInputStream bis = new BufferedInputStream(httpContext.getRequest().getInputStream());
        byte[] buffer = new byte[4096];
        int bytesRead = -1;
        try {
            while ((bytesRead = bis.read(buffer)) != -1) {
                cgiOutputStream.write(buffer);
            }
        }
        catch (IOException ioex) {
            Logger.getLogger(HttpCgiModule.class.getName()).log(Level.SEVERE, null, ioex);
            throw new com.ramforth.webserver.exceptions.IOException(ioex);
        }
        
        httpContext.getResponse().setStatusCode(new HttpStatusCode(HttpStatusCodeClass.SUCCESS, 200, ""));
        
        BufferedInputStream bis2 = new BufferedInputStream(cgiProcess.getInputStream());
        try {
            while ((bytesRead = bis2.read(buffer)) != -1) {
                httpContext.getResponse().getOutputStream().write(buffer);
            }
        }
        catch (IOException ioex) {
            Logger.getLogger(HttpCgiModule.class.getName()).log(Level.SEVERE, null, ioex);
            throw new com.ramforth.webserver.exceptions.IOException(ioex);
        }
        
        return true;
    }
    
}
