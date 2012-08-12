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
  protected String serverPath;
  
  @Override
  public void setRelativePath(String value) {
    this.relativePath = value;
  }

  @Override
  public String getRelativePath() {
    return relativePath;
  }
  
  public void setServerPath(String value) {
    this.serverPath = value;
  }
  
  public String getServerPath() {
    return serverPath;
  }
  
}
