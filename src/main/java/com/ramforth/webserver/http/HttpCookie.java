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

import java.util.Date;
import java.util.Map;

/**
 *
 * @author Tobias Ramforth <tobias.ramforth at tu-dortmund.de>
 */
public class HttpCookie {

    private String domain;
    private Date expires;
    private boolean httpOnly;
    private String name;
    private String path;
    private boolean secure;
    private String value;
    private Map<String, String> values;

    public String getDomain() {
        return domain;
    }

    public void setDomain(String value) {
        this.domain = value;
    }

    public Date getExpires() {
        return expires;
    }

    public void setExpires(Date value) {
        this.expires = value;
    }

    public boolean hasKeys() {
        return false;
    }

    public boolean getHttpOnly() {
        return httpOnly;
    }

    public void setHttpOnly(boolean value) {
        this.httpOnly = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String value) {
        this.name = value;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String value) {
        this.path = value;
    }

    public boolean getSecure() {
        return secure;
    }

    public void setSecure(boolean value) {
        this.secure = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Map<String, String> getValues() {
        return values;
    }

    public void setValues(Map<String, String> value) {
        this.values = value;
    }
}
