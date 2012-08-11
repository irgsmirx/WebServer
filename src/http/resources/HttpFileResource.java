/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package http.resources;

/**
 *
 * @author Tobias Ramforth <tobias.ramforth at tu-dortmund.de>
 */
public class HttpFileResource implements IHttpResource {

  protected String relativePath;

  @Override
  public void setRelativePath(String value) {
    this.relativePath = value;
  }

  @Override
  public String getRelativePath() {
    return relativePath;
  }
  
  
  
}
