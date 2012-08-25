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
import utilities.templates.FileTemplate;
import utilities.templates.ITemplate;
import utilities.templates.StringTemplate;
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
  public boolean processHttpContext(IHttpContext httpContext) {
    if (isGetOrHeadMethod(httpContext.getRequest())) {
      if (resourceExists(httpContext.getRequest().getUri().getPath())) {
        HttpTemplateResource templateResource = getTemplateResource(httpContext.getRequest().getUri().getPath());
        writeTemplateResourceToHttpResponse(httpContext.getResponse(), templateResource);
        return true;
      } else {
        return false;
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
    ITemplate template = templateResource.getTemplate();

    if (template instanceof FileTemplate) {
      writeFileTemplateToHttpResponse(httpResponse, (FileTemplate)template);
    } else if (template instanceof StringTemplate) {
      writeStringTemplateToHttpResponse(httpResponse, (StringTemplate)template);
    }
  }
  
  private void writeFileTemplateToHttpResponse(IHttpResponse httpResponse, FileTemplate fileTemplate) {
    File file = fileTemplate.getTemplate();
    
    if (file.exists()) {
      if (file.canRead()) {
        addHttpHeadersForFileTemplateToResponse(httpResponse, file);
        IHttpResponseWriter httpResponseWriter = new HttpResponseWriter(httpResponse.getOutputStream());
        
        httpResponseWriter.writeResponse(httpResponse);
        
        fileTemplate.renderTo(httpResponse.getOutputStream());
      } else {
        throw new ResourceNotFoundException("Template not found.");
      }      
    } else {
      throw new ResourceNotFoundException("Template not found.");
    }
  }
  
  private void writeStringTemplateToHttpResponse(IHttpResponse httpResponse, StringTemplate stringTemplate) {
    addHttpHeadersForStringTemplateToResponse(httpResponse, stringTemplate.getTemplate());
    IHttpResponseWriter httpResponseWriter = new HttpResponseWriter(httpResponse.getOutputStream());

    httpResponseWriter.writeResponse(httpResponse);

    stringTemplate.renderTo(httpResponse.getOutputStream());
  }
  
  private void addHttpHeadersForFileTemplateToResponse(IHttpResponse httpResponse, File file) {
    httpResponse.setStatusCode(HttpStatusCode.STATUS_200_OK);
    addContentTypeHeaderForFile(httpResponse, file);
    addContentLengthHeaderForFile(httpResponse, file);
  }
  
  private void addHttpHeadersForStringTemplateToResponse(IHttpResponse httpResponse, String string) {
    httpResponse.setStatusCode(HttpStatusCode.STATUS_200_OK);
    addContentTypeHeaderForString(httpResponse);
    addContentLengthHeaderForString(httpResponse, string);
  }
  
  private void addContentTypeHeaderForFile(IHttpResponse httpResponse, File file) {
    String extension = getFileExtensionFromFilename(file.getName());
    String mimeType = getMimeTypeForExtension(extension);
    
    httpResponse.setContentType(mimeType);
  }
  
  private void addContentTypeHeaderForString(IHttpResponse httpResponse) {
    httpResponse.setContentType("text/html");
  }
  
  private void addContentLengthHeaderForFile(IHttpResponse httpResponse, File file) {
    httpResponse.setContentLength(file.length());
  }
  
  private void addContentLengthHeaderForString(IHttpResponse httpResponse, String string) {
    httpResponse.setContentLength(string.getBytes().length);
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

  private String getMimeTypeForExtension(String extension) {
    String mimeType = MimeTypeMap.getInstance().getMimeTypeForExtension(extension);
    if (mimeType == null) {
      mimeType = "application/octet-stream";
    }
    return mimeType;
  }
  
}
