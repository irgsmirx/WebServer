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

import com.ramforth.webserver.http.templates.IWebTemplate;
import java.io.InputStream;

/**
 *
 * @author Tobias Ramforth <tobias.ramforth at tu-dortmund.de>
 */
public class HttpDynamicTemplateResource extends AbstractHttpResource {

    private Class<? extends IWebTemplate> templateType;

    public HttpDynamicTemplateResource(Class<? extends IWebTemplate> templateType) {
        this.templateType = templateType;
    }

    public Class<? extends IWebTemplate> getTemplateType() {
        return templateType;
    }

    public void setTemplateServerPath(Class<? extends IWebTemplate> value) {
        this.templateType = value;
    }

    @Override
    public InputStream tryOpenStream() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
