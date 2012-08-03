/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package exceptions;

/**
 *
 * @author Tobias Ramforth <tobias.ramforth at tu-dortmund.de>
 */
public class AlreadyListeningException extends RuntimeException {

  public AlreadyListeningException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }

  public AlreadyListeningException(Throwable cause) {
    super(cause);
  }

  public AlreadyListeningException(String message, Throwable cause) {
    super(message, cause);
  }

  public AlreadyListeningException(String message) {
    super(message);
  }

  public AlreadyListeningException() {
    super();
  }
  
}
