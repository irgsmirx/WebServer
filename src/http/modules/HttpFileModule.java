/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package http.modules;

import exceptions.HttpException;
import exceptions.ResourceNotFoundException;
import http.HttpMethod;
import http.HttpResponseWriter;
import http.HttpStatusCode;
import http.IHttpContext;
import http.IHttpRequest;
import http.IHttpResponse;
import http.IHttpResponseWriter;
import http.resources.HttpFileResource;
import http.resources.HttpFileResourceProvider;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import utilities.common.implementation.SystemProperties;
import web.MimeTypeMap;

/**
 *
 * @author Tobias Ramforth <tobias.ramforth at tu-dortmund.de>
 */
public class HttpFileModule extends AbstractHttpModule {
 
  public HttpFileModule() {
    super();
    this.resourceProvider = new HttpFileResourceProvider();
  }
  
  @Override
  public void processHttpContext(IHttpContext httpContext) {
    if (isGetOrHeadMethod(httpContext.getRequest())) {
      if (fileResourceExists(httpContext.getRequest().getUri().getPath())) {
        HttpFileResource fileResource = getFileResource(httpContext.getRequest().getUri().getPath());
        writeFileResourceToHttpResponse(httpContext.getResponse(), fileResource);
      } else {
        throw new ResourceNotFoundException("Resource not found.");
      }
    } else {
      throw new HttpException(HttpStatusCode.STATUS_405_METHOD_NOT_ALLOWED, "Error");
    }
  }
  
  private boolean isGetOrHeadMethod(IHttpRequest httpRequest) {
    return httpRequest.getMethod() == HttpMethod.GET
            || httpRequest.getMethod() == HttpMethod.HEAD;
  }
  
  private boolean fileResourceExists(String uriPath) {
    return resourceProvider.containsResource(uriPath);
  }
  
  private HttpFileResource getFileResource(String uriPath) {
    return (HttpFileResource) getResources().getResource(uriPath);
  }
  
  private void writeFileResourceToHttpResponse(IHttpResponse httpResponse, HttpFileResource fileResource) {
    File file = new File(fileResource.getServerPath());
    
    if (file.exists()) {
      if (file.canRead()) {
        addHttpHeadersForFileToResponse(httpResponse, file);
        IHttpResponseWriter httpResponseWriter = new HttpResponseWriter(httpResponse.getOutputStream());
        
        int r = -1;
        FileReader fr;
        try {
          fr = new FileReader(file);
          try {
            while ((r = fr.read()) != -1) {
              httpResponse.getOutputStream().write(r);
            }
          } catch (IOException ex) {
            Logger.getLogger(HttpFileModule.class.getName()).log(Level.SEVERE, null, ex);
          }
        } catch (FileNotFoundException ex) {
          Logger.getLogger(HttpFileModule.class.getName()).log(Level.SEVERE, null, ex);
        }
      } else {
        throw new ResourceNotFoundException("File not found.");
      }      
    } else {
      throw new ResourceNotFoundException("File not found.");
    }
  }
  
  private void addHttpHeadersForFileToResponse(IHttpResponse httpResponse, File file) {
    addContentTypeHeaderForFile(httpResponse, file);
    addContentLengthHeaderForFile(httpResponse, file);
  }
  
  private void addContentTypeHeaderForFile(IHttpResponse httpResponse, File file) {
    String extension = getFileExtensionFromFilename(file.getName());
    
    String mimeType = "application/octet-stream";
    if (extension.compareTo("") != 0) {
      String mimeType2 = MimeTypeMap.getInstance().getMimeTypeForExtension(extension);
      if (mimeType2 != null) {
        mimeType = mimeType2;
      }
    }
    
    httpResponse.setContentType(mimeType);
  }
  
  private void addContentLengthHeaderForFile(IHttpResponse httpResponse, File file) {
    httpResponse.setContentLength(file.length());
  }
  
  private String getFileExtensionFromFilename(String filename) {
    int length = filename.length();
    
    int num = length;
    while (--num >= 0) {
      int c = filename.codePointAt(num);
      if (c == '.') {
        return filename.substring(num, length - num);
      }
      if (c == SystemProperties.FILE_SEPARATOR.codePointAt(0)) {
        break;
      }
    }
    
    return "";
  }
  
  public HttpFileResourceProvider getResources() {
    return (HttpFileResourceProvider)resourceProvider;
  }

  @Override
  public boolean isHandled() {
    return handled;
  }
  
}