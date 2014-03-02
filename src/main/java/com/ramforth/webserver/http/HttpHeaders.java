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

import java.util.Iterator;
import java.util.TreeMap;

/**
 *
 * @author Tobias Ramforth <tobias.ramforth at tu-dortmund.de>
 */
public class HttpHeaders implements IHttpHeaders {

    private TreeMap<String, IHttpHeader> headers = new TreeMap<>();

    @Override
    public void addHeader(IHttpHeader value) {
        String loweredName = value.getName().toLowerCase();
        if (headers.containsKey(loweredName)) {
            headers.get(loweredName).setRawValue(value.getRawValue());
        } else {
            headers.put(loweredName, value);
        }
    }

    @Override
    public void removeHeader(String name) {
        headers.remove(name.toLowerCase());
    }

    @Override
    public IHttpHeader getHeader(String name) {
        return headers.get(name.toLowerCase());
    }

    @Override
    public boolean contains(String name) {
        return headers.containsKey(name.toLowerCase());
    }

    @Override
    public void clear() {
        headers.clear();
    }

    @Override
    public int numberOfHeaders() {
        return headers.size();
    }

    @Override
    public Iterator<IHttpHeader> iterator() {
        return headers.values().iterator();
    }
}
