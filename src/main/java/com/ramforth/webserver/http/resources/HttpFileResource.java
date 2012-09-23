/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ramforth.webserver.http.resources;

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
}
