/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package http;

import web.IMessage;

/**
 *
 * @author Tobias Ramforth <tobias.ramforth at tu-dortmund.de>
 */
public interface IHttpMessage extends IMessage {
  
  IHttpVersion getVersion();
  void setVersion(IHttpVersion value);
  
  IHttpHeaders getHeaders();
    
  long getContentLength();
  void setContentLength(long value);
  
  String getContentType();
  void setContentType(String value);
  
}
