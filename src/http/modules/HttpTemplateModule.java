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
import http.resources.HttpTemplateResource;
import http.resources.HttpTemplateResourceProvider;
import java.io.File;
import utilities.common.implementation.SystemProperties;
import utilities.templates.ITemplate;
import web.MimeTypeMap;

/**
 *
 * @author Tobias Ramforth <tobias.ramforth at tu-dortmund.de>
 */
public class HttpTemplateModule extends AbstractHttpModule {
 
  public HttpTemplateModule() {
    super();
    this.resourceProvider = new HttpTemplateResourceProvider();
  }
  
  @Override
  public void processHttpContext(IHttpContext httpContext) {
    if (isGetOrHeadMethod(httpContext.getRequest())) {
      if (resourceExists(httpContext.getRequest().getUri().getPath())) {
        HttpTemplateResource templateResource = getTemplateResource(httpContext.getRequest().getUri().getPath());
        writeTemplateResourceToHttpResponse(httpContext.getResponse(), templateResource);
        
        handled = true;
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
  
  private HttpTemplateResource getTemplateResource(String uriPath) {
    return (HttpTemplateResource) getResource(uriPath);
  }
  
  private void writeTemplateResourceToHttpResponse(IHttpResponse httpResponse, HttpTemplateResource templateResource) {
    File file = new File(templateResource.getServerPath());
    
    if (file.exists()) {
      if (file.canRead()) {
        ITemplate template = templateResource.getTemplate();
                
        addHttpHeadersForTemplateToResponse(httpResponse, file);
        IHttpResponseWriter httpResponseWriter = new HttpResponseWriter(httpResponse.getOutputStream());
        
        httpResponseWriter.writeResponse(httpResponse);
        
        template.renderTo(httpResponse.getOutputStream());
      } else {
        throw new ResourceNotFoundException("Template not found.");
      }      
    } else {
      throw new ResourceNotFoundException("Template not found.");
    }
  }
  
  private void addHttpHeadersForTemplateToResponse(IHttpResponse httpResponse, File file) {
    httpResponse.setStatusCode(HttpStatusCode.STATUS_200_OK);
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
    
    int num = length - 1;
    while (num >= 0) {
      int c = filename.codePointAt(num);
      if (c == '.') {
        return filename.substring(num + 1, length);
      }
      if (c == SystemProperties.getFileSeparator().codePointAt(0)) {
        break;
      }
      num--;
    }
    
    return "";
  }
  
  public HttpTemplateResourceProvider getTemplateResources() {
    return (HttpTemplateResourceProvider)resourceProvider;
  }
  
}
