/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ramforth.webserver.http;

import com.ramforth.webserver.web.IResponse;

/**
 *
 * @author Tobias Ramforth <tobias.ramforth at tu-dortmund.de>
 */
public interface IHttpResponse extends IHttpMessage, IResponse {

    IHttpStatusCode getStatusCode();

    void setStatusCode(IHttpStatusCode value);

    void redirect(String destination);
}
