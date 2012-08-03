/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package web;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Tobias Ramforth <tobias.ramforth at tu-dortmund.de>
 */
public class HeaderList implements IHeaderList {

  private List<IHeader> headers = new ArrayList<>();
  
  @Override
  public void addHeader(IHeader value) {
    headers.add(value);
  }

  @Override
  public void removeHeader(IHeader value) {
    headers.remove(value);
  }

  @Override
  public void clear() {
    headers.clear();
  }
  
}
