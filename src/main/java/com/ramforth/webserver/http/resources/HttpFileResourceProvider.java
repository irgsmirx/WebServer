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
package com.ramforth.webserver.http.resources;

import com.ramforth.webserver.exceptions.ResourceNotFoundException;
import java.util.Map;
import java.util.TreeMap;

/**
 *
 * @author Tobias Ramforth <tobias.ramforth at tu-dortmund.de>
 */
public class HttpFileResourceProvider implements IHttpResourceProvider {

    protected Map<String, IHttpResource> resources = new TreeMap<>();
    protected IHttpResource defaultResource = null;

    @Override
    public void addResource(IHttpResource value) {
        if (resources.containsKey(value.getRelativePath().toLowerCase())) {
        } else {
            resources.put(value.getRelativePath().toLowerCase(), value);
        }
    }

    @Override
    public void removeResource(IHttpResource value) {
        resources.remove(value.getRelativePath().toLowerCase());
    }

    @Override
    public boolean containsResource(IHttpResource value) {
        try {
            return (defaultResource != null && "/".compareTo(value.getRelativePath().toLowerCase()) == 0) || resources.containsKey(value.getRelativePath().toLowerCase());
        }
        catch (NullPointerException npex) {
            return false;
        }
    }

    @Override
    public boolean containsResource(String relativePath) {
        try {
            return (defaultResource != null && "/".compareTo(relativePath) == 0) || resources.containsKey(relativePath.toLowerCase());
        }
        catch (NullPointerException npex) {
            return false;
        }
    }

    @Override
    public IHttpResource getResource(String relativePath) {
        try {
            if (defaultResource != null && "/".compareTo(relativePath) == 0) {
                return defaultResource;
            } else {
                return resources.get(relativePath.toLowerCase());
            }
        }
        catch (NullPointerException npex) {
            throw new ResourceNotFoundException(npex);
        }
    }

    @Override
    public void clearResources() {
        resources.clear();
        defaultResource = null;
    }

    @Override
    public void setDefaultResource(IHttpResource value) {
        try {
            IHttpResource found = resources.get(value.getRelativePath().toLowerCase());
            this.defaultResource = found;
        }
        catch (NullPointerException npex) {
            throw new ResourceNotFoundException(npex);
        }
    }
}
