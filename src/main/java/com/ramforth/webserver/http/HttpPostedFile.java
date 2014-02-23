/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
