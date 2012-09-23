/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ramforth.webserver.exceptions;

/**
 *
 * @author Tobias Ramforth <tobias.ramforth at tu-dortmund.de>
 */
public class UnknownHostException extends RuntimeException {

  public UnknownHostException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }

  public UnknownHostException(Throwable cause) {
    super(cause);
  }

  public UnknownHostException(String message, Throwable cause) {
    super(message, cause);
  }

  public UnknownHostException(String message) {
    super(message);
  }

  public UnknownHostException() {
    super();
  }
  
}
