/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package http;

import java.net.InetAddress;

/**
 *
 * @author Tobias Ramforth <tobias.ramforth at tu-dortmund.de>
 */
public interface IHttpListener {
  
  void addContextHandler(IHttpContextHandler value);
  void removeContextHandler(IHttpContextHandler value);
  void clearContextHandlers();
  
  int getPort();
  void setPort(int value);
  
  int getBacklog();
  void setBacklog(int value);
  
  InetAddress getListenAddress();
  void setListenAddress(InetAddress value);
  
  void startListening();
  void stopListening();

  boolean isListening();
  
}
