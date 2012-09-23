/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ramforth.webserver.http.headers.entity;

import com.ramforth.webserver.http.headers.LongHttpHeader;

/**
 *
 * @author Tobias Ramforth <tobias.ramforth at tu-dortmund.de>
 */
public class ContentLocationHttpHeader extends LongHttpHeader {

    public static final String CONTENT_LOCATION = "Content-Location";

    public ContentLocationHttpHeader(long value) {
        super(CONTENT_LOCATION, value);
    }
}
