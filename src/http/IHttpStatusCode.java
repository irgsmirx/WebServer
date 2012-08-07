/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package http;

/**
 *
 * @author Tobias Ramforth <tobias.ramforth at tu-dortmund.de>
 */
public interface IHttpStatusCode {
 
  HttpStatusCodeClass getStatusCodeClass();
  void setStatusCodeClass(HttpStatusCodeClass value);
  
  int getCode();
  void setCode(int value);
  
  String getReason();
  void setReason(String value);
  
}
