/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package http.sessionState;

/**
 *
 * @author Tobias Ramforth <tobias.ramforth at tu-dortmund.de>
 */
public enum SessionStateMode {
  Off,
  InProc,
  StateServer,
  SQLServer,
  Custom
}
