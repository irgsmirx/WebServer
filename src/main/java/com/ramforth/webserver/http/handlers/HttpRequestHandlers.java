/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ramforth.webserver.http.handlers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author Tobias Ramforth <tobias.ramforth at tu-dortmund.de>
 */
public class HttpRequestHandlers implements IHttpRequestHandlers {

  private List<IHttpRequestHandler> handlers = new ArrayList<>();
  
  @Override
  public void add(IHttpRequestHandler handler) {
    handlers.add(handler);
  }

  @Override
  public void remove(IHttpRequestHandler handler) {
    handlers.remove(handler);
  }

  @Override
  public IHttpRequestHandler getAt(int index) {
    return handlers.get(index);
  }

  @Override
  public int numberOfHandlers() {
    return handlers.size();
  }

  @Override
  public Iterator<IHttpRequestHandler> iterator() {
    return handlers.iterator();
  }
  
}
