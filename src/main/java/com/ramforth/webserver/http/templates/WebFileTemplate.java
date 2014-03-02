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

import com.ramforth.utilities.templates.FileTemplate;
import com.ramforth.webserver.http.IHttpContext;
import com.ramforth.webserver.web.MimeTypeMap;
import org.apache.commons.io.FilenameUtils;

import java.io.File;

/**
 *
 * @author Tobias Ramforth <tobias.ramforth at tu-dortmund.de>
 */
public class WebFileTemplate extends FileTemplate implements IWebTemplate {

    protected IHttpContext context;

    public WebFileTemplate(File file) {
        super(file);
    }

    public WebFileTemplate(File file, String placeholderBeginTag, String placeholderEndTag, String escapeCharacter) {
        super(file, placeholderBeginTag, placeholderEndTag, escapeCharacter);
    }

    public WebFileTemplate(String filePath) {
        super(filePath);
    }

    public WebFileTemplate(String filePath, String placeholderBeginTag, String placeholderEndTag, String escapeCharacter) {
        super(filePath, placeholderBeginTag, placeholderEndTag, escapeCharacter);
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

    private String getMimeTypeForFile() {
        String extension = FilenameUtils.getExtension(((File) getTemplate()).getName());
        return getMimeTypeForExtension(extension);
    }

    private String getMimeTypeForExtension(String extension) {
        String mimeType = MimeTypeMap.getInstance().getMimeTypeForExtension(extension);
        if (mimeType == null) {
            mimeType = "application/octet-stream";
        }
        return mimeType;
    }

    @Override
    public String getContentType() {
        return getMimeTypeForFile();
    }

}
