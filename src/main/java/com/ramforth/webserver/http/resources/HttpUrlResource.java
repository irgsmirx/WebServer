/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ramforth.webserver.http.resources;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Tobias Ramforth <tobias.ramforth at tu-dortmund.de>
 */
public class HttpUrlResource extends AbstractHttpResource {

    protected URL serverURL;

    public void setServerURL(URL value) {
        this.serverURL = value;
    }

    public URL getServerURL() {
        return serverURL;
    }

    @Override
    public InputStream tryOpenStream() {
        try {
            return serverURL.openStream();
        }
        catch (IOException ex) {
            Logger.getLogger(HttpUrlResource.class.getName()).log(Level.SEVERE, null, ex);
            throw new com.ramforth.utilities.exceptions.IOException(ex);
        }
    }

}
