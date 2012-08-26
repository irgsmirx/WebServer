/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package http.templates;

import http.IHttpContext;
import java.io.File;
import utilities.templates.FileTemplate;

/**
 *
 * @author Tobias Ramforth <tobias.ramforth at tu-dortmund.de>
 */
public class WebFileTemplate extends FileTemplate {
  
  protected IHttpContext context;
  
  public WebFileTemplate(File file) {
    super(file);
  }
  
  public WebFileTemplate(File file, char placeholderBeginTag, char placeholderEndTag, String escapeCharacter) {
    super(file, placeholderBeginTag, placeholderEndTag, escapeCharacter);
  }
  
  public WebFileTemplate(String filePath) {
    super(filePath);
  }

  public WebFileTemplate(String filePath, char placeholderBeginTag, char placeholderEndTag, String escapeCharacter) {
    super(filePath, placeholderBeginTag, placeholderEndTag, escapeCharacter);
  }

  public IHttpContext getContext() {
    return context;
  }

  public void setContext(IHttpContext context) {
    this.context = context;
  }

  public void load() {
  }

}
