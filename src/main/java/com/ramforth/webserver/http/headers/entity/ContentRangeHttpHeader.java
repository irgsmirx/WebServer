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
public class ContentRangeHttpHeader extends LongHttpHeader {

    public static final String CONTENT_RANGE = "Content-Range";

    public ContentRangeHttpHeader(long value) {
        super(CONTENT_RANGE, value);
    }
}
