/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ramforth.webserver.http.modules;

import com.ramforth.webserver.exceptions.HttpException;
import com.ramforth.webserver.http.HttpResponseWriter;
import com.ramforth.webserver.http.HttpStatusCode;
import com.ramforth.webserver.http.IHttpContext;
import com.ramforth.webserver.http.IHttpResponse;
import com.ramforth.webserver.http.IHttpResponseWriter;
import com.ramforth.webserver.http.resources.HttpStaticTemplateResource;
import com.ramforth.webserver.http.resources.HttpStaticTemplateResourceProvider;
import com.ramforth.utilities.templates.FileTemplate;
import com.ramforth.utilities.templates.ITemplate;
import com.ramforth.utilities.templates.StringTemplate;
import com.ramforth.webserver.web.MimeTypeMap;
import org.apache.commons.io.FilenameUtils;

import java.io.File;

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
            writeFileTemplateToHttpResponse(httpResponse, (FileTemplate) template);
        } else if (template instanceof StringTemplate) {
            writeStringTemplateToHttpResponse(httpResponse, (StringTemplate) template);
        }
    }

    private void writeTemplateResourceHeadersToHttpResponse(IHttpResponse httpResponse, HttpStaticTemplateResource templateResource) {
        ITemplate template = templateResource.getTemplate();

        if (template instanceof FileTemplate) {
            writeFileTemplateHeadersToHttpResponse(httpResponse, (FileTemplate) template);
        } else if (template instanceof StringTemplate) {
            writeStringTemplateHeadersToHttpResponse(httpResponse, (StringTemplate) template);
        }
    }

    private void writeFileTemplateHeadersToHttpResponse(IHttpResponse httpResponse, FileTemplate fileTemplate) {
        File file = fileTemplate.getTemplate();

        assertFileExists(file);
        assertFileIsReadable(file);

        addHttpHeadersForFileTemplateToResponse(httpResponse, fileTemplate);
        IHttpResponseWriter httpResponseWriter = new HttpResponseWriter(httpResponse.getOutputStream());

        httpResponseWriter.writeResponse(httpResponse);
    }

    private void writeFileTemplateToHttpResponse(IHttpResponse httpResponse, FileTemplate fileTemplate) {
        writeFileTemplateHeadersToHttpResponse(httpResponse, fileTemplate);

        fileTemplate.renderTo(httpResponse.getOutputStream());
    }

    private void writeStringTemplateHeadersToHttpResponse(IHttpResponse httpResponse, StringTemplate stringTemplate) {
        addHttpHeadersForStringTemplateToResponse(httpResponse, stringTemplate);
        IHttpResponseWriter httpResponseWriter = new HttpResponseWriter(httpResponse.getOutputStream());

        httpResponseWriter.writeResponse(httpResponse);
    }

    private void writeStringTemplateToHttpResponse(IHttpResponse httpResponse, StringTemplate stringTemplate) {
        writeStringTemplateHeadersToHttpResponse(httpResponse, stringTemplate);

        stringTemplate.renderTo(httpResponse.getOutputStream());
    }

    private void addHttpHeadersForFileTemplateToResponse(IHttpResponse httpResponse, FileTemplate fileTemplate) {
        httpResponse.setStatusCode(HttpStatusCode.STATUS_200_OK);
        addContentTypeHeaderForFile(httpResponse, fileTemplate.getTemplate());
        addContentLengthHeaderForTemplate(httpResponse, fileTemplate);
    }

    private void addHttpHeadersForStringTemplateToResponse(IHttpResponse httpResponse, StringTemplate stringTemplate) {
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

    private void addContentLengthHeaderForTemplate(IHttpResponse httpResponse, FileTemplate fileTemplate) {
        httpResponse.setContentLength(fileTemplate.getLength());
    }

    private void addContentLengthHeaderForString(IHttpResponse httpResponse, String string) {
        httpResponse.setContentLength(string.getBytes().length);
    }

    private void addContentLengthHeaderForStringTemplate(IHttpResponse httpResponse, StringTemplate stringTemplate) {
        httpResponse.setContentLength(stringTemplate.getLength());
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
