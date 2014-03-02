/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ramforth.webserver.http;

import java.nio.charset.Charset;

/**
 *
 * @author Tobias Ramforth <tobias.ramforth at tu-dortmund.de>
 */
public interface IDispositionType {

    String getType();

    void setType(String value);

    NameValueMap getParameters();

    void addParameter(String name, String value);

    void removeParameter(String name);

    void clearParameters();

    int numberOfParameters();

    String getValue(String name);

    boolean containsParameter(String name);

    Charset getCharset();

    String getName();

}
