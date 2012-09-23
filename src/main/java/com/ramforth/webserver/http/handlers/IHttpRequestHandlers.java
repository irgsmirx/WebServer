/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ramforth.webserver.http.handlers;

/**
 *
 * @author Tobias Ramforth <tobias.ramforth at tu-dortmund.de>
 */
public interface IHttpRequestHandlers extends Iterable<IHttpRequestHandler> {

    void add(IHttpRequestHandler handler);

    void remove(IHttpRequestHandler handler);

    IHttpRequestHandler getAt(int index);

    int numberOfHandlers();
}
