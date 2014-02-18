/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ramforth.webserver.http.resources;

/**
 *
 * @author Tobias Ramforth <tobias.ramforth at tu-dortmund.de>
 */
public abstract class AbstractHttpResource implements IHttpResource {

    protected String relativePath;

    @Override
    public void setRelativePath(String value) {
        this.relativePath = value;
    }

    @Override
    public String getRelativePath() {
        return relativePath;
    }
    
}
