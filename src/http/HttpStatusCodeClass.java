/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package http;

/**
 *
 * @author Tobias Ramforth <tobias.ramforth at tu-dortmund.de>
 */
public enum HttpStatusCodeClass {
  // 1xx: Informational - Request received, continuing process
  INFORMATIONAL,
  // 2xx: Success - The action was successfully received, understood, and accepted
  SUCCESS,
  // 3xx: Redirection - Further action must be taken in order to complete the request
  REDIRECTION,
  // 4xx: Client Error - The request contains bad syntax or cannot be fulfilled
  CLIENT_ERROR,
  // 5xx: Server Error - The server failed to fulfill an apparently valid request
  SERVER_ERROR
}
