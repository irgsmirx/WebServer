/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ramforth.webserver.http.modules;

import com.ramforth.webserver.http.resources.HttpDynamicTemplateResource;
import com.ramforth.webserver.http.templates.IWebTemplate;
import com.ramforth.webserver.http.templates.WebFileTemplate;

/**
 *
 * @author Tobias Ramforth <tobias.ramforth at tu-dortmund.de>
 */
public interface IWebTemplateInstantiator {
  
  IWebTemplate instantiate(HttpDynamicTemplateResource templateResource);
  
}
