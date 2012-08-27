/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package http.modules;

import http.resources.HttpDynamicTemplateResource;
import http.templates.WebFileTemplate;

/**
 *
 * @author Tobias Ramforth <tobias.ramforth at tu-dortmund.de>
 */
public interface IWebFileTemplateInstantiator {
  
  WebFileTemplate instantiate(HttpDynamicTemplateResource templateResource);
  
}
