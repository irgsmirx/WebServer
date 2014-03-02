/* 
 * Copyright (C) 2014 Tobias Ramforth
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.ramforth.webserver.http.modules;

import com.ramforth.webserver.exceptions.HttpException;
import com.ramforth.webserver.exceptions.ResourceNotFoundException;
import com.ramforth.webserver.http.*;
import com.ramforth.webserver.http.resources.HttpFileResource;
import com.ramforth.webserver.http.resources.HttpFileResourceProvider;
import com.ramforth.webserver.web.MimeTypeMap;
import java.io.*;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Tobias Ramforth <tobias.ramforth at tu-dortmund.de>
 */
public class HttpFileModule extends AbstractHttpModule {

    private static final Logger LOGGER = LoggerFactory.getLogger(HttpFileModule.class);

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
            throw new HttpException(HttpStatusCode.STATUS_405_METHOD_NOT_ALLOWED, "Method not allowed");
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
            }
            catch (IOException ex) {
                LOGGER.warn("Could not read from file or could not write to output stream.", ex);
                throw new HttpException(HttpStatusCode.STATUS_500_INTERNAL_SERVER_ERROR, "Could not read file.");
            }
            finally {
            }
        }
        catch (FileNotFoundException ex) {
            LOGGER.warn("File ' + " + file.getPath() + "' not found!", ex);
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
        String extension = FilenameUtils.getExtension(file.getName());

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
        return (HttpFileResourceProvider) resourceProvider;
    }
}
