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
import http.resources.HttpDynamicTemplateResource;
import http.resources.HttpDynamicTemplateResourceProvider;
import http.templates.WebFileTemplate;
import java.io.File;
import utilities.common.implementation.SystemProperties;
import utilities.path.Path;
import utilities.templates.FileTemplate;
import utilities.templates.StringTemplate;
import web.MimeTypeMap;

/**
 *
 * @author Tobias Ramforth <tobias.ramforth at tu-dortmund.de>
 */
public class HttpDynamicTemplateModule extends AbstractHttpModule {
 
  private IWebFileTemplateInstantiator instantiator = new DefaultWebFileTemplateInstantiator();
  
  public HttpDynamicTemplateModule() {
    super();
    this.resourceProvider = new HttpDynamicTemplateResourceProvider();
  }
  
  @Override
  public boolean processHttpContext(IHttpContext httpContext) {
    if (isGetOrHeadOrPostMethod(httpContext.getRequest())) {
      if (resourceExists(httpContext.getRequest().getUri().getPath())) {
        HttpDynamicTemplateResource templateResource = getTemplateResource(httpContext.getRequest().getUri().getPath());

        WebFileTemplate template = instantiator.instantiate(templateResource);
        template.setContext(httpContext);
        template.load();
        
        switch (httpContext.getRequest().getMethod()) {
          case GET:
            writeFileTemplateToHttpResponse(httpContext.getResponse(), template);
            break;
          case HEAD:
            writeFileTemplateHeadersToHttpResponse(httpContext.getResponse(), template);
            break;
          case POST:
            writeFileTemplateToHttpResponse(httpContext.getResponse(), template);
            break;
        }
        return true;
      } else {
        return false;
      }
    } else {
      throw new HttpException(HttpStatusCode.STATUS_405_METHOD_NOT_ALLOWED, "Error");
    }
  }
  
  private HttpDynamicTemplateResource getTemplateResource(String uriPath) {
    return (HttpDynamicTemplateResource) getResource(uriPath);
  }
  
  private void writeFileTemplateToHttpResponse(IHttpResponse httpResponse, FileTemplate fileTemplate) {
    writeFileTemplateHeadersToHttpResponse(httpResponse, fileTemplate);

    fileTemplate.renderTo(httpResponse.getOutputStream());
  }
  
  private void writeFileTemplateHeadersToHttpResponse(IHttpResponse httpResponse, FileTemplate fileTemplate) {
    File file = fileTemplate.getTemplate();
    
    assertFileExists(file);
    assertFileIsReadable(file);
    
    addHttpHeadersForFileTemplateToResponse(httpResponse, file);
    IHttpResponseWriter httpResponseWriter = new HttpResponseWriter(httpResponse.getOutputStream());

    httpResponseWriter.writeResponse(httpResponse);
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
    String extension = Path.getFileExtensionFromFilename(file.getName());
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

  public HttpDynamicTemplateResourceProvider getTemplateResources() {
    return (HttpDynamicTemplateResourceProvider)resourceProvider;
  }

  private String getMimeTypeForExtension(String extension) {
    String mimeType = MimeTypeMap.getInstance().getMimeTypeForExtension(extension);
    if (mimeType == null) {
      mimeType = "application/octet-stream";
    }
    return mimeType;
  }
  
  public void setInstantiator(IWebFileTemplateInstantiator value) {
    this.instantiator = value;
  }
  
}
