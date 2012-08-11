/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package http.resources;

/**
 *
 * @author Tobias Ramforth <tobias.ramforth at tu-dortmund.de>
 */
public interface IHttpResourceProvider {
  
  void addResource(IHttpResource value);
  void removeResource(IHttpResource value);
  boolean containsResource(IHttpResource value);
  
}
