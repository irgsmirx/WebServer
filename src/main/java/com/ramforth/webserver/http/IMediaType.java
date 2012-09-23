/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ramforth.webserver.http;

/**
 *
 * @author Tobias Ramforth <tobias.ramforth at tu-dortmund.de>
 */
public interface IMediaType {

    String getType();

    void setType(String value);

    NameValueMap getParameters();

    void addParameter(String name, String value);

    void removeParameter(String name);

    void clearParameters();

    int numberOfParameters();
}
