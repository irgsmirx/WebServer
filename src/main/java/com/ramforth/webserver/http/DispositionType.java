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

import java.nio.charset.Charset;

/**
 *
 * @author Tobias Ramforth <tobias.ramforth at tu-dortmund.de>
 */
public class DispositionType implements IDispositionType {

    private static final String DEFAULT_MEDIA_TYPE = "application/octet-stream";

    protected String type = DEFAULT_MEDIA_TYPE;
    protected NameValueMap parameters = new NameValueMap();

    @Override
    public String getType() {
        return type;
    }

    @Override
    public void setType(String value) {
        this.type = value;
    }

    @Override
    public NameValueMap getParameters() {
        return parameters;
    }

    @Override
    public void addParameter(String name, String value) {
        parameters.add(name, value);
    }

    @Override
    public void removeParameter(String name) {
        parameters.remove(name);
    }

    @Override
    public void clearParameters() {
        parameters.clear();
    }

    @Override
    public int numberOfParameters() {
        return parameters.numberOfEntries();
    }

    @Override
    public String getValue(String name) {
        return parameters.get(name);
    }

    @Override
    public boolean containsParameter(String name) {
        return parameters.containsName(name);
    }

    @Override
    public Charset getCharset() {
        try {
            String charsetString = parameters.get("charset");
            return Charset.forName(charsetString);
        }
        catch (Exception ex) {
            return Charset.defaultCharset();
        }
    }

    @Override
    public String getName() {
        return parameters.get("name");
    }

}
