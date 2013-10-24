/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ramforth.webserver.http.modules;

import com.ramforth.utilities.templates.FileTemplate;
import com.ramforth.utilities.templates.ITemplate;
import com.ramforth.utilities.templates.StringTemplate;
import com.ramforth.webserver.exceptions.HttpException;
import com.ramforth.webserver.http.HttpResponseWriter;
import com.ramforth.webserver.http.HttpStatusCode;
import com.ramforth.webserver.http.IHttpContext;
import com.ramforth.webserver.http.IHttpResponse;
import com.ramforth.webserver.http.IHttpResponseWriter;
import com.ramforth.webserver.http.resources.HttpStaticTemplateResource;
import com.ramforth.webserver.http.resources.HttpStaticTemplateResourceProvider;
import com.ramforth.webserver.http.templates.WebFileTemplate;
import com.ramforth.webserver.http.templates.WebStringTemplate;
import com.ramforth.webserver.web.MimeTypeMap;
import java.io.File;
import org.apache.commons.io.FilenameUtils;

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
            throw new HttpException(HttpStatusCode.STATUS_405_METHOD_NOT_ALLOWED, "Method not allowed");
        }
    }

    private HttpStaticTemplateResource getTemplateResource(String uriPath) {
        return (HttpStaticTemplateResource) getResource(uriPath);
    }

    private void writeTemplateResourceToHttpResponse(IHttpResponse httpResponse, HttpStaticTemplateResource templateResource) {
        ITemplate template = templateResource.getTemplate();

        if (template instanceof WebFileTemplate) {
            writeFileTemplateToHttpResponse(httpResponse, (WebFileTemplate) template);
        } else if (template instanceof WebStringTemplate) {
            writeWebStringTemplateToHttpResponse(httpResponse, (WebStringTemplate) template);
        }
    }

    private void writeTemplateResourceHeadersToHttpResponse(IHttpResponse httpResponse, HttpStaticTemplateResource templateResource) {
        ITemplate template = templateResource.getTemplate();

        if (template instanceof WebFileTemplate) {
            writeFileTemplateHeadersToHttpResponse(httpResponse, (WebFileTemplate) template);
        } else if (template instanceof WebStringTemplate) {
            writeWebStringTemplateHeadersToHttpResponse(httpResponse, (WebStringTemplate) template);
        }
    }

    private void writeFileTemplateHeadersToHttpResponse(IHttpResponse httpResponse, WebFileTemplate fileTemplate) {
        File file = (File)fileTemplate.getTemplate();

        assertFileExists(file);
        assertFileIsReadable(file);

        addHttpHeadersForWebFileTemplateToResponse(httpResponse, fileTemplate);
        IHttpResponseWriter httpResponseWriter = new HttpResponseWriter(httpResponse.getOutputStream());

        httpResponseWriter.writeResponse(httpResponse);
    }

    private void writeFileTemplateToHttpResponse(IHttpResponse httpResponse, WebFileTemplate fileTemplate) {
        writeFileTemplateHeadersToHttpResponse(httpResponse, fileTemplate);

        fileTemplate.renderTo(httpResponse.getOutputStream());
    }

    private void writeWebStringTemplateHeadersToHttpResponse(IHttpResponse httpResponse, WebStringTemplate stringTemplate) {
        addHttpHeadersForWebStringTemplateToResponse(httpResponse, stringTemplate);
        IHttpResponseWriter httpResponseWriter = new HttpResponseWriter(httpResponse.getOutputStream());

        httpResponseWriter.writeResponse(httpResponse);
    }

    private void writeWebStringTemplateToHttpResponse(IHttpResponse httpResponse, WebStringTemplate stringTemplate) {
        writeWebStringTemplateHeadersToHttpResponse(httpResponse, stringTemplate);

        stringTemplate.renderTo(httpResponse.getOutputStream());
    }

    private void addHttpHeadersForWebFileTemplateToResponse(IHttpResponse httpResponse, WebFileTemplate fileTemplate) {
        httpResponse.setStatusCode(HttpStatusCode.STATUS_200_OK);
        addContentTypeHeaderForFile(httpResponse, (File)fileTemplate.getTemplate());
        addContentLengthHeaderForWebFileTemplate(httpResponse, fileTemplate);
    }

    private void addHttpHeadersForWebStringTemplateToResponse(IHttpResponse httpResponse, WebStringTemplate stringTemplate) {
        httpResponse.setStatusCode(HttpStatusCode.STATUS_200_OK);
        addContentTypeHeaderForString(httpResponse);
        addContentLengthHeaderForStringTemplate(httpResponse, stringTemplate);
    }

    private void addContentTypeHeaderForFile(IHttpResponse httpResponse, File file) {
        String extension = FilenameUtils.getExtension(file.getName());
        String mimeType = getMimeTypeForExtension(extension);

        httpResponse.setContentType(mimeType);
    }

    private void addContentTypeHeaderForString(IHttpResponse httpResponse) {
        httpResponse.setContentType("text/html");
    }

    private void addContentLengthHeaderForFile(IHttpResponse httpResponse, File file) {
        httpResponse.setContentLength(file.length());
    }

    private void addContentLengthHeaderForWebFileTemplate(IHttpResponse httpResponse, WebFileTemplate fileTemplate) {
        httpResponse.setContentLength(fileTemplate.getLengthInBytes());
    }

    private void addContentLengthHeaderForStringTemplate(IHttpResponse httpResponse, WebStringTemplate stringTemplate) {
        httpResponse.setContentLength(stringTemplate.getLengthInBytes());
    }

    public HttpStaticTemplateResourceProvider getTemplateResources() {
        return (HttpStaticTemplateResourceProvider) resourceProvider;
    }

    private String getMimeTypeForExtension(String extension) {
        String mimeType = MimeTypeMap.getInstance().getMimeTypeForExtension(extension);
        if (mimeType == null) {
            mimeType = "application/octet-stream";
        }
        return mimeType;
    }
}
