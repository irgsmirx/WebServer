/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package http.modules;

import http.resources.HttpDynamicTemplateResource;
import http.templates.IWebTemplate;
import http.templates.WebFileTemplate;

/**
 *
 * @author Tobias Ramforth <tobias.ramforth at tu-dortmund.de>
 */
public interface IWebTemplateInstantiator {
  
  IWebTemplate instantiate(HttpDynamicTemplateResource templateResource);
  
}
