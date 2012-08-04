/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package http;

/**
 *
 * @author Tobias Ramforth <tobias.ramforth at tu-dortmund.de>
 */
public class HttpVersion implements IHttpVersion {

  private int major;
  private int minor;
  
  public HttpVersion(int major, int minor) {
    this.major = major;
    this.minor = minor;
  }
  
  @Override
  public int getMajor() {
    return major;
  }

  @Override
  public void setMajor(int value) {
    this.major = value;
  }

  @Override
  public int getMinor() {
    return minor;
  }

  @Override
  public void setMinor(int value) {
    this.minor = value;
  }
  
}
