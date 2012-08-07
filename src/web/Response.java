/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package web;

/**
 *
 * @author Tobias Ramforth <tobias.ramforth at tu-dortmund.de>
 */
public class Response extends Message implements IResponse {

  protected ConnectionType connectionType;
  
  @Override
  public ConnectionType getConnectionType() {
    return connectionType;
  }

  @Override
  public void setConnectionType(ConnectionType value) {
    this.connectionType = value;
  }
  
}
