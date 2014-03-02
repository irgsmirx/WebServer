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
package com.ramforth.webserver.http;

import java.net.URI;

/**
 *
 * @author Tobias Ramforth <tobias.ramforth at tu-dortmund.de>
 */
public class HttpRequestLine implements IHttpRequestLine {

    private HttpMethod method;
    private IHttpVersion version;
    private URI uri;

    public HttpRequestLine(HttpMethod method, IHttpVersion version, URI uri) {
        this.method = method;
        this.version = version;
        this.uri = uri;
    }

    @Override
    public HttpMethod getMethod() {
        return method;
    }

    @Override
    public void setMethod(HttpMethod value) {
        this.method = value;
    }

    @Override
    public IHttpVersion getVersion() {
        return version;
    }

    @Override
    public void setVersion(IHttpVersion value) {
        this.version = value;
    }

    @Override
    public URI getURI() {
        return uri;
    }

    @Override
    public void setURI(URI value) {
        this.uri = value;
    }
}
