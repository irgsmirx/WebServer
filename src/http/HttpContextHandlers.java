/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package http;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author Tobias Ramforth <tobias.ramforth at tu-dortmund.de>
 */
public class HttpContextHandlers implements IHttpContextHandlers {

  private List<IHttpContextHandler> handlers = new ArrayList<>();
  
  @Override
  public void add(IHttpContextHandler handler) {
    handlers.add(handler);
  }

  @Override
  public void remove(IHttpContextHandler handler) {
    handlers.remove(handler);
  }

  @Override
  public IHttpContextHandler getAt(int index) {
    return handlers.get(index);
  }

  @Override
  public int numberOfHandlers() {
    return handlers.size();
  }

  @Override
  public Iterator<IHttpContextHandler> iterator() {
    return handlers.iterator();
  }
  
}
