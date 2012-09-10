/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package http.modules;

import exceptions.HttpException;
import http.HttpResponseWriter;
import http.HttpStatusCode;
import http.IHttpContext;
import http.IHttpResponse;
import http.IHttpResponseWriter;
import http.resources.HttpStaticTemplateResource;
import http.resources.HttpStaticTemplateResourceProvider;
import java.io.File;
import utilities.path.Path;
import utilities.templates.FileTemplate;
import utilities.templates.ITemplate;
import utilities.templates.StringTemplate;
import web.MimeTypeMap;

/**
 *
 * @author Tobias Ramforth <tobias.ramforth at tu-dortmund.de>
 */
public class HttpStaticTemplateModule extends AbstractHttpModule {
 
  public HttpStaticTemplateModule() {
    super();
    this.resourceProvider = new HttpStaticTemplateResourceProvider();
  }
  
  @Override
  public boolean processHttpContext(IHttpContext httpContext) {
    if (isGetOrHeadOrPostMethod(httpContext.getRequest())) {
      if (resourceExists(httpContext.getRequest().getUri().getPath())) {
        HttpStaticTemplateResource templateResource = getTemplateResource(httpContext.getRequest().getUri().getPath());
    
        switch (httpContext.getRequest().getMethod()) {
          case GET:
            writeTemplateResourceToHttpResponse(httpContext.getResponse(), templateResource);
            break;
          case HEAD:
            writeTemplateResourceHeadersToHttpResponse(httpContext.getResponse(), templateResource);
            break;
          case POST:
            writeTemplateResourceToHttpResponse(httpContext.getResponse(), templateResource);
            break;
        }
        return true;
      } else {
        throw new HttpException(HttpStatusCode.STATUS_404_NOT_FOUND, "Resource not found!");
      }
    } else {
      throw new HttpException(HttpStatusCode.STATUS_405_METHOD_NOT_ALLOWED, "Error");
    }
  }
 
  private HttpStaticTemplateResource getTemplateResource(String uriPath) {
    return (HttpStaticTemplateResource) getResource(uriPath);
  }
  
  private void writeTemplateResourceToHttpResponse(IHttpResponse httpResponse, HttpStaticTemplateResource templateResource) {
    ITemplate template = templateResource.getTemplate();

    if (template instanceof FileTemplate) {
      writeFileTemplateToHttpResponse(httpResponse, (FileTemplate)template);
    } else if (template instanceof StringTemplate) {
      writeStringTemplateToHttpResponse(httpResponse, (StringTemplate)template);
    }
  }
  
  private void writeTemplateResourceHeadersToHttpResponse(IHttpResponse httpResponse, HttpStaticTemplateResource templateResource) {
    ITemplate template = templateResource.getTemplate();

    if (template instanceof FileTemplate) {
      writeFileTemplateHeadersToHttpResponse(httpResponse, (FileTemplate)template);
    } else if (template instanceof StringTemplate) {
      writeStringTemplateHeadersToHttpResponse(httpResponse, (StringTemplate)template);
    }
  }
  
  private void writeFileTemplateHeadersToHttpResponse(IHttpResponse httpResponse, FileTemplate fileTemplate) {
    File file = fileTemplate.getTemplate();

    assertFileExists(file);
    assertFileIsReadable(file);

    addHttpHeadersForFileTemplateToResponse(httpResponse, file);
    IHttpResponseWriter httpResponseWriter = new HttpResponseWriter(httpResponse.getOutputStream());

    httpResponseWriter.writeResponse(httpResponse);
  }
  
  private void writeFileTemplateToHttpResponse(IHttpResponse httpResponse, FileTemplate fileTemplate) {
    writeFileTemplateHeadersToHttpResponse(httpResponse, fileTemplate);

    fileTemplate.renderTo(httpResponse.getOutputStream());
  }

  private void writeStringTemplateHeadersToHttpResponse(IHttpResponse httpResponse, StringTemplate stringTemplate) {
    addHttpHeadersForStringTemplateToResponse(httpResponse, stringTemplate.getTemplate());
    IHttpResponseWriter httpResponseWriter = new HttpResponseWriter(httpResponse.getOutputStream());

    httpResponseWriter.writeResponse(httpResponse);
  }

  private void writeStringTemplateToHttpResponse(IHttpResponse httpResponse, StringTemplate stringTemplate) {
    writeStringTemplateHeadersToHttpResponse(httpResponse, stringTemplate);
    
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
  
  public HttpStaticTemplateResourceProvider getTemplateResources() {
    return (HttpStaticTemplateResourceProvider)resourceProvider;
  }

  private String getMimeTypeForExtension(String extension) {
    String mimeType = MimeTypeMap.getInstance().getMimeTypeForExtension(extension);
    if (mimeType == null) {
      mimeType = "application/octet-stream";
    }
    return mimeType;
  }
  
}
