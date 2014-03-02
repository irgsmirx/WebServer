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
package com.ramforth.webserver.http.templates;

import com.ramforth.utilities.templates.StringTemplate;
import com.ramforth.webserver.http.IHttpContext;

/**
 *
 * @author Tobias Ramforth <tobias.ramforth at tu-dortmund.de>
 */
public class WebStringTemplate extends StringTemplate implements IWebTemplate {

    private static final String DEFAULT_CONTENT_TYPE = "text/html";
    protected IHttpContext context;
    protected String contentType = DEFAULT_CONTENT_TYPE;

    public WebStringTemplate(String template) {
        super(template);
    }

    @Override
    public IHttpContext getContext() {
        return context;
    }

    @Override
    public void setContext(IHttpContext context) {
        this.context = context;
    }

    @Override
    public void load() {
    }

    @Override
    public void post() {
    }

    @Override
    public void get() {
    }

    @Override
    public final String getContentType() {
        return contentType;
    }

}
