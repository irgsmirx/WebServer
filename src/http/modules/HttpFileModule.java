/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package http.modules;

import exceptions.HttpException;
import exceptions.ResourceNotFoundException;
import http.HttpResponseWriter;
import http.HttpStatusCode;
import http.IHttpContext;
import http.IHttpResponse;
import http.IHttpResponseWriter;
import http.resources.HttpFileResource;
import http.resources.HttpFileResourceProvider;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import utilities.path.Path;
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
  public boolean processHttpContext(IHttpContext httpContext) {
    if (isGetOrHeadOrPostMethod(httpContext.getRequest())) {
      if (resourceExists(httpContext.getRequest().getUri().getPath())) {
        HttpFileResource fileResource = getFileResource(httpContext.getRequest().getUri().getPath());
        
        switch (httpContext.getRequest().getMethod()) {
          case GET:
            writeFileResourceToHttpResponse(httpContext.getResponse(), fileResource);
            break;
          case HEAD:
            writeFileResourceHeadersToHttpResponse(httpContext.getResponse(), fileResource);
            break;
          case POST:
            writeFileResourceToHttpResponse(httpContext.getResponse(), fileResource);
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
  
  private HttpFileResource getFileResource(String uriPath) {
    return (HttpFileResource) getResource(uriPath);
  }
  
  private void writeFileToHttpResponse(IHttpResponse httpResponse, File file) {
    assertFileExists(file);
    assertFileIsReadable(file);
    
    addHttpHeadersForFileToResponse(httpResponse, file);

    IHttpResponseWriter httpResponseWriter = new HttpResponseWriter(httpResponse.getOutputStream());
    httpResponseWriter.writeResponse(httpResponse);
    
    int r;
    InputStream is;
    try {
      is = new FileInputStream(file);
      try {
        while ((r = is.read()) != -1) {
          httpResponse.getOutputStream().write(r);
        }
      } catch (IOException ex) {
        Logger.getLogger(HttpFileModule.class.getName()).log(Level.SEVERE, null, ex);
        throw new HttpException(HttpStatusCode.STATUS_500_INTERNAL_SERVER_ERROR, "Could not read file.");
      } finally {
      }
    } catch (FileNotFoundException ex) {
      Logger.getLogger(HttpFileModule.class.getName()).log(Level.SEVERE, null, ex);
      throw new ResourceNotFoundException("File not found.");
    }
  }
  
  private void writeFileHeadersToHttpResponse(IHttpResponse httpResponse, File file) {
    assertFileExists(file);
    assertFileIsReadable(file);
    
    addHttpHeadersForFileToResponse(httpResponse, file);

    IHttpResponseWriter httpResponseWriter = new HttpResponseWriter(httpResponse.getOutputStream());
    httpResponseWriter.writeResponse(httpResponse);
  }
  
  private void writeFileResourceToHttpResponse(IHttpResponse httpResponse, HttpFileResource fileResource) {
    writeFileToHttpResponse(httpResponse, new File(fileResource.getServerPath()));
  }
  
  private void writeFileResourceHeadersToHttpResponse(IHttpResponse httpResponse, HttpFileResource fileResource) {
    writeFileHeadersToHttpResponse(httpResponse, new File(fileResource.getServerPath()));
  }
  
  private void addHttpHeadersForFileToResponse(IHttpResponse httpResponse, File file) {
    httpResponse.setStatusCode(HttpStatusCode.STATUS_200_OK);
    addContentTypeHeaderForFile(httpResponse, file);
    addContentLengthHeaderForFile(httpResponse, file);
  }
  
  private void addContentTypeHeaderForFile(IHttpResponse httpResponse, File file) {
    String extension = Path.getFileExtensionFromFilename(file.getName());
    
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
    
  public HttpFileResourceProvider getFileResources() {
    return (HttpFileResourceProvider)resourceProvider;
  }

}
