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
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;
import java.util.logging.Logger;
import utilities.common.implementation.SystemProperties;
import utilities.templates.FileTemplate;
import utilities.templates.StringTemplate;
import web.MimeTypeMap;

/**
 *
 * @author Tobias Ramforth <tobias.ramforth at tu-dortmund.de>
 */
public class HttpDynamicTemplateModule extends AbstractHttpModule {
 
  public HttpDynamicTemplateModule() {
    super();
    this.resourceProvider = new HttpDynamicTemplateResourceProvider();
  }
  
  @Override
  public boolean processHttpContext(IHttpContext httpContext) {
    if (isGetOrHeadMethod(httpContext.getRequest())) {
      if (resourceExists(httpContext.getRequest().getUri().getPath())) {
        HttpDynamicTemplateResource templateResource = getTemplateResource(httpContext.getRequest().getUri().getPath());
        
        try {
          Constructor<? extends WebFileTemplate> constructor = templateResource.getTemplateType().getConstructor(new Class<?>[0]);
          constructor.setAccessible(true);
          WebFileTemplate template = constructor.newInstance(new Object[0]);
          template.setContext(httpContext);
          template.load();
          writeFileTemplateToHttpResponse(httpContext.getResponse(), template);
        } catch (InstantiationException ex) {
          Logger.getLogger(HttpDynamicTemplateModule.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
          Logger.getLogger(HttpDynamicTemplateModule.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalArgumentException ex) {
          Logger.getLogger(HttpDynamicTemplateModule.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvocationTargetException ex) {
          Logger.getLogger(HttpDynamicTemplateModule.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchMethodException ex) {
          Logger.getLogger(HttpDynamicTemplateModule.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SecurityException ex) {
          Logger.getLogger(HttpDynamicTemplateModule.class.getName()).log(Level.SEVERE, null, ex);
        }
        
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
  
  private HttpDynamicTemplateResource getTemplateResource(String uriPath) {
    return (HttpDynamicTemplateResource) getResource(uriPath);
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
  
}
