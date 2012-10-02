/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ramforth.webserver.http.modules;

import com.ramforth.utilities.templates.StringTemplate;
import com.ramforth.webserver.exceptions.HttpException;
import com.ramforth.webserver.http.HttpResponseWriter;
import com.ramforth.webserver.http.HttpStatusCode;
import com.ramforth.webserver.http.IHttpContext;
import com.ramforth.webserver.http.IHttpResponse;
import com.ramforth.webserver.http.IHttpResponseWriter;
import com.ramforth.webserver.http.resources.HttpDynamicTemplateResource;
import com.ramforth.webserver.http.resources.HttpDynamicTemplateResourceProvider;
import com.ramforth.webserver.http.templates.IWebTemplate;
import com.ramforth.webserver.http.templates.WebFileTemplate;
import com.ramforth.webserver.http.templates.WebStringTemplate;
import java.io.File;

/**
 *
 * @author Tobias Ramforth <tobias.ramforth at tu-dortmund.de>
 */
public class HttpDynamicTemplateModule extends AbstractHttpModule {

    private IWebTemplateInstantiator fileInstantiator = new DefaultWebTemplateInstantiator();

    public HttpDynamicTemplateModule() {
        super();
        this.resourceProvider = new HttpDynamicTemplateResourceProvider();
    }

    @Override
    public boolean processHttpContext(IHttpContext httpContext) {
        if (isGetOrHeadOrPostMethod(httpContext.getRequest())) {
            if (resourceExists(httpContext.getRequest().getUri().getPath())) {
                HttpDynamicTemplateResource templateResource = getTemplateResource(httpContext.getRequest().getUri().getPath());


                IWebTemplate template = fileInstantiator.instantiate(templateResource);
                template.setContext(httpContext);
                template.load();

                switch (httpContext.getRequest().getMethod()) {
                        case GET:
                        case HEAD:
                            template.get();
                            break;
                        case POST:
                            template.post();
                            break;                    
                }
                
                if (template instanceof WebFileTemplate) {
                    switch (httpContext.getRequest().getMethod()) {
                        case GET:
                            writeWebFileTemplateToHttpResponse(httpContext.getResponse(), (WebFileTemplate) template);
                            break;
                        case HEAD:
                            writeWebFileTemplateHeadersToHttpResponse(httpContext.getResponse(), (WebFileTemplate) template);
                            break;
                        case POST:
                            writeWebFileTemplateToHttpResponse(httpContext.getResponse(), (WebFileTemplate) template);
                            break;
                    }
                } else if (template instanceof WebStringTemplate) {
                    switch (httpContext.getRequest().getMethod()) {
                        case GET:
                            writeStringTemplateToHttpResponse(httpContext.getResponse(), (WebStringTemplate) template);
                            break;
                        case HEAD:
                            writeStringTemplateHeadersToHttpResponse(httpContext.getResponse(), (WebStringTemplate) template);
                            break;
                        case POST:
                            writeStringTemplateToHttpResponse(httpContext.getResponse(), (WebStringTemplate) template);
                            break;
                    }
                }
                return true;
            } else {
                throw new HttpException(HttpStatusCode.STATUS_404_NOT_FOUND, "Resource not found!");
            }
        } else {
            throw new HttpException(HttpStatusCode.STATUS_405_METHOD_NOT_ALLOWED, "Error");
        }
    }

    private HttpDynamicTemplateResource getTemplateResource(String uriPath) {
        return (HttpDynamicTemplateResource) getResource(uriPath);
    }

    private void writeWebFileTemplateToHttpResponse(IHttpResponse httpResponse, WebFileTemplate webFileTemplate) {
        writeWebFileTemplateHeadersToHttpResponse(httpResponse, webFileTemplate);

        webFileTemplate.renderTo(httpResponse.getOutputStream());
    }

    private void writeWebFileTemplateHeadersToHttpResponse(IHttpResponse httpResponse, WebFileTemplate webFileTemplate) {
        File file = webFileTemplate.getTemplate();

        assertFileExists(file);
        assertFileIsReadable(file);

        addHttpHeadersForWebFileTemplateToResponse(httpResponse, webFileTemplate);
        IHttpResponseWriter httpResponseWriter = new HttpResponseWriter(httpResponse.getOutputStream());

        httpResponseWriter.writeResponse(httpResponse);
    }

    private void writeStringTemplateHeadersToHttpResponse(IHttpResponse httpResponse, StringTemplate stringTemplate) {
        addHttpHeadersForStringTemplateToResponse(httpResponse, stringTemplate);
        IHttpResponseWriter httpResponseWriter = new HttpResponseWriter(httpResponse.getOutputStream());

        httpResponseWriter.writeResponse(httpResponse);
    }

    private void writeStringTemplateToHttpResponse(IHttpResponse httpResponse, StringTemplate stringTemplate) {
        addHttpHeadersForStringTemplateToResponse(httpResponse, stringTemplate);
        IHttpResponseWriter httpResponseWriter = new HttpResponseWriter(httpResponse.getOutputStream());

        httpResponseWriter.writeResponse(httpResponse);

        stringTemplate.renderTo(httpResponse.getOutputStream());
    }

    private void addHttpHeadersForWebFileTemplateToResponse(IHttpResponse httpResponse, WebFileTemplate webFileTemplate) {
        httpResponse.setStatusCode(HttpStatusCode.STATUS_200_OK);
        addContentTypeHeaderForFileTemplate(httpResponse, webFileTemplate);
        addContentLengthHeaderForTemplate(httpResponse, webFileTemplate);
    }

    private void addHttpHeadersForStringTemplateToResponse(IHttpResponse httpResponse, StringTemplate stringTemplate) {
        httpResponse.setStatusCode(HttpStatusCode.STATUS_200_OK);
        addContentTypeHeaderForString(httpResponse);
        addContentLengthHeaderForStringTemplate(httpResponse, stringTemplate);
    }

    private void addContentTypeHeaderForFileTemplate(IHttpResponse httpResponse, WebFileTemplate webFileTemplate) {
        httpResponse.setContentType(webFileTemplate.getContentType());
    }

    private void addContentTypeHeaderForString(IHttpResponse httpResponse) {
        httpResponse.setContentType("text/html");
    }

    private void addContentTypeHeaderForJSON(IHttpResponse httpResponse) {
        httpResponse.setContentType("application/json");
    }

    private void addContentLengthHeaderForFile(IHttpResponse httpResponse, File file) {
        httpResponse.setContentLength(file.length());
    }

    private void addContentLengthHeaderForTemplate(IHttpResponse httpResponse, WebFileTemplate webFileTemplate) {
        httpResponse.setContentLength(webFileTemplate.getLength());
    }

    private void addContentLengthHeaderForString(IHttpResponse httpResponse, String string) {
        httpResponse.setContentLength(string.getBytes().length);
    }

    private void addContentLengthHeaderForStringTemplate(IHttpResponse httpResponse, StringTemplate stringTemplate) {
        httpResponse.setContentLength(stringTemplate.getLength());
    }

    public HttpDynamicTemplateResourceProvider getTemplateResources() {
        return (HttpDynamicTemplateResourceProvider) resourceProvider;
    }

    public void setInstantiator(IWebTemplateInstantiator value) {
        this.fileInstantiator = value;
    }
}
