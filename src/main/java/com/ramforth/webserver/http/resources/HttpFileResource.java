/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ramforth.webserver.http.resources;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Tobias Ramforth <tobias.ramforth at tu-dortmund.de>
 */
public class HttpFileResource extends AbstractHttpResource {

    protected String serverPath;

    public void setServerPath(String value) {
        this.serverPath = value;
    }

    public String getServerPath() {
        return serverPath;
    }

    @Override
    public InputStream tryOpenStream() {
        try {
            return new FileInputStream(serverPath);
        }
        catch (FileNotFoundException ex) {
            Logger.getLogger(HttpFileResource.class.getName()).log(Level.SEVERE, null, ex);
            throw new com.ramforth.utilities.exceptions.IOException(ex);
        }
    }

}
