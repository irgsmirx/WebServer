/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package http;

/**
 *
 * @author Tobias Ramforth <tobias.ramforth at tu-dortmund.de>
 */
public class HttpHeaderFactory implements IHttpHeaderFactory {

  @Override
  public IHttpHeader buildHttpHeader(String name, String rawValue) {
    IHttpHeader header;
    
    switch (name.toLowerCase()) {
      case "host":
        header = new HostHttpHeader(rawValue);
        break;
      case "http_x_requested_with":
        header = new AjaxHttpHeader();
        break;
      default:
        header = new StringHttpHeader(name, rawValue);
        break;
    }
    
    return header;    
  }
  
}
