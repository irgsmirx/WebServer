/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ramforth.webserver.http.modules;

import com.ramforth.webserver.http.resources.HttpDynamicTemplateResource;
import com.ramforth.webserver.http.templates.IWebTemplate;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
            LOGGER.warn("Could not instantiate WebTemplate.", ex);
            throw new RuntimeException(ex);
        }
        catch (IllegalAccessException ex) {
            LOGGER.warn("Constructor object is enforcing Java language access control and the underlying constructor is inaccessible.", ex);
            throw new RuntimeException(ex);
        }
        catch (IllegalArgumentException ex) {
            LOGGER.warn("The number of actual and formal parameters differ or an unwrapping conversion for primitive arguments failed or, after possible unwrapping, a parameter value could not be converted to the corresponding formal parameter type by a method invocation conversion or this constructor pertains to an enum type.", ex);
            throw new RuntimeException(ex);
        }
        catch (InvocationTargetException ex) {
            LOGGER.warn("Underlying constructor threw an exception.", ex);
            throw new RuntimeException(ex);
        }
        catch (NoSuchMethodException ex) {
            LOGGER.warn("There is no matching constructor for the given arguments.", ex);
            throw new RuntimeException(ex);
        }
        catch (SecurityException ex) {
            LOGGER.warn("Could not make WebTemplate constructor accessible.", ex);
            throw new RuntimeException(ex);
        }
    }
}
