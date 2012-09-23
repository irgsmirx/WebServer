/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ramforth.webserver.http.headers;

import com.ramforth.webserver.http.HttpHeader;

/**
 *
 * @author Tobias Ramforth <tobias.ramforth at tu-dortmund.de>
 */
public class StringHttpHeader extends HttpHeader {

    public StringHttpHeader(String name, String rawValue) {
        super(name, rawValue);
    }

    public String getValue() {
        return rawValue;
    }

    public void setValue(String value) {
        this.rawValue = value;
    }
}
