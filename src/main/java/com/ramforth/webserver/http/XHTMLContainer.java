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

import java.io.IOException;
import java.io.PrintStream;

public class XHTMLContainer implements WebContainer {

    //private XHTMLDocument document;
    private String contentType = "text/html";
    private String filename;

    /**
     * @return Returns the filename.
     */
    public String getFilename() {
        return filename;
    }

    /**
     * @param filename The filename to set.
     */
    public void setFilename(String filename) {
        this.filename = filename;
    }

    public XHTMLContainer() {
    }

    @Override
    public void print(PrintStream p) throws IOException {
        //if (document != null) {
        //	document.printXML(p);
        //}
    }

    @Override
    public boolean isConsistent() {
        //return (document != null);
        return false;
    }

    /**
     * @return Returns the document.
     */
//	public XHTMLDocument getDocument() {
//		return document;
//	}
    /**
     * @param document The document to set.
     */
//	public void setDocument(XHTMLDocument document) {
//		this.document = document;
//	}
    /**
     * @return Returns the contentType.
     */
    @Override
    public String getContentType() {
        return contentType;
    }

    /**
     * @param contentType The contentType to set.
     */
    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    @Override
    public long getContentLength() {
        StringBuilder sb = new StringBuilder();
        //document.appendXML(sb);

        return sb.length();
    }
}
