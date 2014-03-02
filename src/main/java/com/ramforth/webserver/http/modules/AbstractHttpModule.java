/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ramforth.webserver.http.modules;

import com.ramforth.webserver.exceptions.ResourceNotFoundException;
import com.ramforth.webserver.http.HttpMethod;
import com.ramforth.webserver.http.IHttpRequest;
import com.ramforth.webserver.http.resources.IHttpResource;
import com.ramforth.webserver.http.resources.IHttpResourceProvider;
import java.io.File;

/**
 *
 * @author Tobias Ramforth <tobias.ramforth at tu-dortmund.de>
 */
public abstract class AbstractHttpModule implements IHttpModule {

    protected IHttpResourceProvider resourceProvider;

    public IHttpResourceProvider getResources() {
        return resourceProvider;
    }

    protected boolean resourceExists(String uriPath) {
        return resourceProvider.containsResource(uriPath);
    }

    protected IHttpResource getResource(String uriPath) {
        return resourceProvider.getResource(uriPath);
    }

    protected void assertFileExists(File file) {
        if (!file.exists()) {
            throw new ResourceNotFoundException("File not found.");
        }
    }

    protected void assertFileIsReadable(File file) {
        if (!file.canRead()) {
            throw new ResourceNotFoundException("File not found.");
        }
    }

    protected boolean isGetOrHeadOrPostMethod(IHttpRequest httpRequest) {
        return httpRequest.getMethod() == HttpMethod.GET
                || httpRequest.getMethod() == HttpMethod.HEAD
                || httpRequest.getMethod() == HttpMethod.POST;
    }
}
