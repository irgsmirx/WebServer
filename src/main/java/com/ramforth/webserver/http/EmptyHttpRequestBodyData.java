/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ramforth.webserver.http;

/**
 *
 * @author tobias
 */
public class EmptyHttpRequestBodyData implements IHttpRequestBodyData {

    @Override
    public void applyTo(IHttpRequest httpRequest) {
        // nothing to do here
    }
    
}
