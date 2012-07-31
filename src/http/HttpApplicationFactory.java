/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package http;

import java.util.Stack;

/**
 *
 * @author Tobias Ramforth <tobias.ramforth at tu-dortmund.de>
 */
public abstract class HttpApplicationFactory {
  
  protected Stack<HttpApplication> applications = new Stack<>();
  
  
  
}
