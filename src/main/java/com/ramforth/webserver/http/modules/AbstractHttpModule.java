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
