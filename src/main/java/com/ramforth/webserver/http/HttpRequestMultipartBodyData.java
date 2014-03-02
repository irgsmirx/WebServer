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

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author tobias
 */
public class HttpRequestMultipartBodyData implements IHttpRequestBodyData {

    private final List<IHttpRequestBodyData> parts = new ArrayList<>();

    @Override
    public void applyTo(IHttpRequest httpRequest) {
        for (IHttpRequestBodyData part : parts) {
            part.applyTo(httpRequest);
        }
    }

    public final Iterable<IHttpRequestBodyData> getParts() {
        return parts;
    }

    public void addPart(IHttpRequestBodyData part) {
        this.parts.add(part);
    }

}
