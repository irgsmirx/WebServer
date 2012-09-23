/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ramforth.webserver.http.modules;

import com.ramforth.webserver.http.resources.HttpDynamicTemplateResource;
import com.ramforth.webserver.http.templates.IWebTemplate;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Tobias Ramforth <tobias.ramforth at tu-dortmund.de>
 */
public class DefaultWebTemplateInstantiator implements IWebTemplateInstantiator {

  @Override
  public IWebTemplate instantiate(HttpDynamicTemplateResource templateResource) {
    try {
      Constructor<? extends IWebTemplate> constructor = templateResource.getTemplateType().getConstructor(new Class<?>[0]);
      constructor.setAccessible(true);
      return constructor.newInstance(new Object[0]);
    } catch (InstantiationException ex) {
      Logger.getLogger(DefaultWebTemplateInstantiator.class.getName()).log(Level.SEVERE, null, ex);
      throw new RuntimeException(ex);
    } catch (IllegalAccessException ex) {
      Logger.getLogger(DefaultWebTemplateInstantiator.class.getName()).log(Level.SEVERE, null, ex);
      throw new RuntimeException(ex);
    } catch (IllegalArgumentException ex) {
      Logger.getLogger(DefaultWebTemplateInstantiator.class.getName()).log(Level.SEVERE, null, ex);
      throw new RuntimeException(ex);
    } catch (InvocationTargetException ex) {
      Logger.getLogger(DefaultWebTemplateInstantiator.class.getName()).log(Level.SEVERE, null, ex);
      throw new RuntimeException(ex);
    } catch (NoSuchMethodException ex) {
      Logger.getLogger(DefaultWebTemplateInstantiator.class.getName()).log(Level.SEVERE, null, ex);
      throw new RuntimeException(ex);
    } catch (SecurityException ex) {
      Logger.getLogger(DefaultWebTemplateInstantiator.class.getName()).log(Level.SEVERE, null, ex);
      throw new RuntimeException(ex);
    }
  }
  
}
