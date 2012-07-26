/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package http;

import java.util.Date;
import java.util.Map;

/**
 *
 * @author Tobias Ramforth <tobias.ramforth at tu-dortmund.de>
 */
public class HttpCookie {
  
  private String domain;
  private Date expires;
  private boolean httpOnly;
  private String name;
  private String path;
  private boolean secure;
  private String value;
  private Map<String, String> values;
  
  public String getDomain() {
    return domain;
  }
  
  public void setDomain(String value) {
    this.domain = value;
  }
  
  public Date getExpires() {
    return expires;
  }
  
  public void setExpires(Date value) {
    this.expires = value;
  }
  
  public boolean hasKeys() {
    return false;
  }
  
  public boolean getHttpOnly() {
    return httpOnly;
  }
  
  public void setHttpOnly(boolean value) {
    this.httpOnly = value;
  }
  
  public String getName() {
    return name;
  }
  
  public void setName(String value) {
    this.name = value;
  }
  
  public String getPath() {
    return path;
  }
  
  public void setPath(String value) {
    this.path = value;
  }
  
  public boolean getSecure() {
    return secure;
  }
  
  public void setSecure(boolean value) {
    this.secure = value;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }
  
  public Map<String, String> getValues() {
    return values;
  }
  
  public void setValues(Map<String, String> value) {
    this.values = value;
  }
  
}
