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
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 *
 * @author tobias
 */
public class HttpPostedFile {

    private int contentLength;
    private final String mimeType;
    private final String filename;
    private final String name;
    private final byte[] data;

    public HttpPostedFile(String name, String filename, String mimeType, byte[] data) {
        this.name = name;
        this.filename = filename;
        this.mimeType = mimeType;
        this.data = data;
    }

    public int getContentLength() {
        return contentLength;
    }

    public String getMimeType() {
        return mimeType;
    }

    public String getFilename() {
        return filename;
    }

    public byte[] getData() {
        return data;
    }

    public InputStream getInputStream() {
        return null;
    }

    public void saveAs(String filename) {
        saveAs(Paths.get(filename));
    }

    public void saveAs(Path filename) {
        try {
            Files.write(filename, data);
        }
        catch (IOException ex) {
            throw new com.ramforth.utilities.exceptions.IOException("Could not save posted file '" + name + "' as '" + filename + "'.", ex);
        }
    }

    public void saveAt(Path parent) {
        Path completePath = parent.resolve(this.filename);
        saveAs(completePath);
    }

}
