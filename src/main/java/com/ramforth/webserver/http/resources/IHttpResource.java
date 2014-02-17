/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ramforth.webserver.http.resources;

import java.io.InputStream;

/**
 *
 * @author Tobias Ramforth <tobias.ramforth at tu-dortmund.de>
 */
public interface IHttpResource {

    void setRelativePath(String value);

    String getRelativePath();
    
    InputStream tryOpenStream();
    
}
