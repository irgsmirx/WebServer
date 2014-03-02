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

/**
 *
 * @author tobias
 */
public class HttpRequestFileBodyData implements IHttpRequestBodyData {

    private final String name;
    private final String filename;
    private final String mimeType;
    private final byte[] data;

    public HttpRequestFileBodyData(String name, String filename, String mimeType, byte[] data) {
        this.name = name;
        this.filename = filename;
        this.mimeType = mimeType;
        this.data = data;
    }

    @Override
    public void applyTo(IHttpRequest httpRequest) {
        HttpPostedFile httpPostedFile = new HttpPostedFile(this.name, this.filename, this.mimeType, this.data);
        ((HttpRequest) httpRequest).postedFiles.put(this.name, httpPostedFile);
    }

}
