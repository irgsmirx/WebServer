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
  
  IHttpHeaders getHeaders();
  
}
