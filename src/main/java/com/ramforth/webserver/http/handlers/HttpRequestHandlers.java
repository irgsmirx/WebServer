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
package com.ramforth.webserver.http.handlers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author Tobias Ramforth <tobias.ramforth at tu-dortmund.de>
 */
public class HttpRequestHandlers implements IHttpRequestHandlers {

    private final List<IHttpRequestHandler> handlers = new ArrayList<>();

    @Override
    public void add(IHttpRequestHandler handler) {
        handlers.add(handler);
    }

    @Override
    public void remove(IHttpRequestHandler handler) {
        handlers.remove(handler);
    }

    @Override
    public IHttpRequestHandler getAt(int index) {
        return handlers.get(index);
    }

    @Override
    public int numberOfHandlers() {
        return handlers.size();
    }

    @Override
    public Iterator<IHttpRequestHandler> iterator() {
        return handlers.iterator();
    }
}
