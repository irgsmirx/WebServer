/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ramforth.webserver.http.modules;

import com.ramforth.webserver.http.IHttpContext;
import com.ramforth.webserver.web.modules.IModule;

/**
 *
 * @author Tobias Ramforth <tobias.ramforth at tu-dortmund.de>
 */
public interface IHttpModule extends IModule {

    boolean processHttpContext(IHttpContext httpContext);
    
}
