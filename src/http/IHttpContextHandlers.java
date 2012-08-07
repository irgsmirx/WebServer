/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package http;

/**
 *
 * @author Tobias Ramforth <tobias.ramforth at tu-dortmund.de>
 */
public interface IHttpContextHandlers extends Iterable<IHttpContextHandler> {
  
  void add(IHttpContextHandler handler);
  void remove(IHttpContextHandler handler);
  IHttpContextHandler getAt(int index);
  int numberOfHandlers();
  
}
