/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package http;

import java.net.InetAddress;
import web.IRequestHandler;

/**
 *
 * @author Tobias Ramforth <tobias.ramforth at tu-dortmund.de>
 */
public interface IHttpListener {
  
  void addRequestHandler(IRequestHandler value);
  void removeRequestHandler(IRequestHandler value);
  void clearRequestHandlers();
  
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
