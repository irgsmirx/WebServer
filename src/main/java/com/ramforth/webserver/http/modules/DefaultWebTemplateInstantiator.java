/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ramforth.webserver.http.modules;

import com.ramforth.webserver.http.resources.HttpDynamicTemplateResource;
import com.ramforth.webserver.http.templates.IWebTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 *
 * @author Tobias Ramforth <tobias.ramforth at tu-dortmund.de>
 */
public class DefaultWebTemplateInstantiator implements IWebTemplateInstantiator {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultWebTemplateInstantiator.class);

    @Override
    public IWebTemplate instantiate(HttpDynamicTemplateResource templateResource) {
        try {
            Constructor<? extends IWebTemplate> constructor = templateResource.getTemplateType().getConstructor(new Class<?>[0]);
            constructor.setAccessible(true);
            return constructor.newInstance(new Object[0]);
        }
        catch (InstantiationException ex) {
            LOGGER.warn("Error", ex); //TODO Enter precise error message
            throw new RuntimeException(ex);
        }
        catch (IllegalAccessException ex) {
            LOGGER.warn("Error", ex); //TODO Enter precise error message
            throw new RuntimeException(ex);
        }
        catch (IllegalArgumentException ex) {
            LOGGER.warn("Error", ex); //TODO Enter precise error message
            throw new RuntimeException(ex);
        }
        catch (InvocationTargetException ex) {
            LOGGER.warn("Error", ex); //TODO Enter precise error message
            throw new RuntimeException(ex);
        }
        catch (NoSuchMethodException ex) {
            LOGGER.warn("Error", ex); //TODO Enter precise error message
            throw new RuntimeException(ex);
        }
        catch (SecurityException ex) {
            LOGGER.warn("Error", ex); //TODO Enter precise error message
            throw new RuntimeException(ex);
        }
    }
}
