/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package http.modules;

/**
 *
 * @author Tobias Ramforth <tobias.ramforth at tu-dortmund.de>
 */
public interface IHttpModules extends Iterable<IHttpModule> {
  
  void add(IHttpModule module);
  void remove(IHttpModule module);
  IHttpModule getAt(int index);
  int numberOfModules();
  
}
