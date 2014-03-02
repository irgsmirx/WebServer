/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ramforth.webserver.http.modules;

import com.ramforth.utilities.common.implementation.StringUtilities;
import com.ramforth.webserver.exceptions.HttpException;
import com.ramforth.webserver.exceptions.ResourceNotFoundException;
import com.ramforth.webserver.http.*;
import com.ramforth.webserver.http.resources.HttpFileResourceProvider;
import com.ramforth.webserver.http.resources.HttpUrlResource;
import com.ramforth.webserver.web.MimeTypeMap;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Tobias Ramforth <tobias.ramforth at tu-dortmund.de>
 */
public class HttpUrlModule extends AbstractHttpModule {

    private static final Logger LOGGER = LoggerFactory.getLogger(HttpUrlModule.class);

    public HttpUrlModule() {
        super();
        this.resourceProvider = new HttpFileResourceProvider();
    }

    @Override
    public boolean processHttpContext(IHttpContext httpContext) {
        if (isGetOrHeadOrPostMethod(httpContext.getRequest())) {
            if (resourceExists(httpContext.getRequest().getUri().getPath())) {
                HttpUrlResource urlResource = getUrlResource(httpContext.getRequest().getUri().getPath());

                switch (httpContext.getRequest().getMethod()) {
                    case GET:
                        writeUrlResourceToHttpResponse(httpContext.getResponse(), urlResource);
                        break;
                    case HEAD:
                        writeUrlResourceHeadersToHttpResponse(httpContext.getResponse(), urlResource);
                        break;
                    case POST:
                        writeUrlResourceToHttpResponse(httpContext.getResponse(), urlResource);
                        break;
                }
                return true;
            } else {
                throw new HttpException(HttpStatusCode.STATUS_404_NOT_FOUND, "Resource not found!");
            }
        } else {
            throw new HttpException(HttpStatusCode.STATUS_405_METHOD_NOT_ALLOWED, "Method not allowed");
        }
    }

    private HttpUrlResource getUrlResource(String uriPath) {
        return (HttpUrlResource) getResource(uriPath);
    }

    private void writeUrlToHttpResponse(IHttpResponse httpResponse, URL url) {
        try {
            URLConnection urlConnection = url.openConnection();

            addHttpHeadersForUrlToResponse(httpResponse, urlConnection);

            IHttpResponseWriter httpResponseWriter = new HttpResponseWriter(httpResponse.getOutputStream());
            httpResponseWriter.writeResponse(httpResponse);

            InputStream is = urlConnection.getInputStream();
            int r = -1;
            try {
                while ((r = is.read()) != -1) {
                    httpResponse.getOutputStream().write(r);
                }
            }
            catch (IOException ex) {
                LOGGER.warn("Could not read from URL or could not write to output stream.", ex);
                throw new HttpException(HttpStatusCode.STATUS_500_INTERNAL_SERVER_ERROR, "Could not read URL.");
            }
            finally {
                if (is != null) {
                    is.close();
                }
            }
        }
        catch (IOException ex) {
            LOGGER.warn("Colud not read from URL ' + " + url.getPath() + "'!", ex);
            throw new ResourceNotFoundException("File not found.");
        }
    }

    private void writeUrlHeadersToHttpResponse(IHttpResponse httpResponse, URL url) {
        try {
            URLConnection urlConnection = url.openConnection();

            addHttpHeadersForUrlToResponse(httpResponse, urlConnection);

            IHttpResponseWriter httpResponseWriter = new HttpResponseWriter(httpResponse.getOutputStream());
            httpResponseWriter.writeResponse(httpResponse);
        }
        catch (IOException ioex) {
            LOGGER.warn("Colud not read from URL ' + " + url.getPath() + "'!", ioex);
            throw new ResourceNotFoundException("File not found.");
        }
    }

    private void writeUrlResourceToHttpResponse(IHttpResponse httpResponse, HttpUrlResource urlResource) {
        writeUrlToHttpResponse(httpResponse, urlResource.getServerURL());
    }

    private void writeUrlResourceHeadersToHttpResponse(IHttpResponse httpResponse, HttpUrlResource urlResource) {
        writeUrlHeadersToHttpResponse(httpResponse, urlResource.getServerURL());
    }

    private void addHttpHeadersForUrlToResponse(IHttpResponse httpResponse, URLConnection urlConnection) {
        httpResponse.setStatusCode(HttpStatusCode.STATUS_200_OK);
        addContentTypeHeaderForURL(httpResponse, urlConnection);
        addContentLengthHeaderForURL(httpResponse, urlConnection);
    }

    private void addContentTypeHeaderForURL(IHttpResponse httpResponse, URLConnection urlConnection) {
        String mimeType = urlConnection.getContentType();

        if (StringUtilities.isNullOrEmpty(mimeType) || "content/unknown".equals(mimeType)) {
            String extension = FilenameUtils.getExtension(urlConnection.getURL().getPath());

            mimeType = "application/octet-stream";
            if (!StringUtilities.isNullOrEmpty(extension)) {
                String mimeType2 = MimeTypeMap.getInstance().getMimeTypeForExtension(extension);
                if (mimeType2 != null) {
                    mimeType = mimeType2;
                }
            }
        }

        httpResponse.setContentType(mimeType);
    }

    private void addContentLengthHeaderForURL(IHttpResponse httpResponse, URLConnection urlConnection) {
        httpResponse.setContentLength(urlConnection.getContentLengthLong());
    }

    public HttpFileResourceProvider getFileResources() {
        return (HttpFileResourceProvider) resourceProvider;
    }
}
