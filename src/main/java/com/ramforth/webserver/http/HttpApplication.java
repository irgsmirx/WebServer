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

import com.ramforth.webserver.exceptions.HttpException;

/**
 *
 * @author Tobias Ramforth <tobias.ramforth at tu-dortmund.de>
 */
public class HttpApplication {

    private HttpContext context;
    private HttpSessionState session;

    public IHttpRequest getRequest() {
        IHttpRequest httpRequest = null;

        if (context != null) {
            httpRequest = context.getRequest();
        }

        if (httpRequest == null) {
            throw new HttpException("Request not available");
        }

        return httpRequest;
    }

    public IHttpResponse getResponse() {
        IHttpResponse httpResponse = null;

        if (context != null) {
            httpResponse = context.getResponse();
        }

        if (httpResponse == null) {
            throw new HttpException("Response not available");
        }

        return httpResponse;
    }

    public HttpSessionState getSession() {
        HttpSessionState httpSessionState = null;

        if (session != null) {
            httpSessionState = session;
        } else {
            if (context != null) {
                httpSessionState = context.getSession();
            }
        }

        if (httpSessionState == null) {
            throw new HttpException("Session not available");
        }

        return httpSessionState;
    }
}
