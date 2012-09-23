/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ramforth.webserver.http;

import com.ramforth.webserver.web.IHeader;

/**
 *
 * @author Tobias Ramforth <tobias.ramforth at tu-dortmund.de>
 */
public interface IHttpHeader extends IHeader {

    String getName();

    void setName(String value);

    String getRawValue();

    void setRawValue(String value);
}
