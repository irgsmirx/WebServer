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

import com.ramforth.utilities.common.implementation.Pair;
import com.ramforth.webserver.web.IRequest;
import java.net.InetAddress;
import java.net.URI;

/**
 *
 * @author Tobias Ramforth <tobias.ramforth at tu-dortmund.de>
 */
public interface IHttpRequest extends IHttpMessage, IRequest {

    HttpMethod getMethod();

    void setMethod(HttpMethod value);

    URI getUri();

    void setUri(URI value);

    NameValueMap getQueryString();

    String getQueryString(String key);

    void setQueryString(String key, String value);

    NameValueMap getForm();

    String getForm(String key);

    void setForm(String key, String value);

    NameValueMap getParams();

    String getParams(String key);

    Iterable<HttpPostedFile> getPostedFiles();

    HttpPostedFile getPostedFile(String name);

    int getNumberOfPostedFiles();

    String getClientHostAddress();
    void setClientHostAddress(String value);
    String getClientHostName();
    void setClientHostName(String value);
    
}
