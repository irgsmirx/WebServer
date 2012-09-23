/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ramforth.webserver.http.templates;

import com.ramforth.webserver.http.IHttpContext;
import java.io.File;
import com.ramforth.utilities.path.Path;
import com.ramforth.utilities.templates.FileTemplate;
import com.ramforth.webserver.web.MimeTypeMap;

/**
 *
 * @author Tobias Ramforth <tobias.ramforth at tu-dortmund.de>
 */
public class WebFileTemplate extends FileTemplate implements IWebTemplate {
  
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

	@Override
  public IHttpContext getContext() {
    return context;
  }

	@Override
  public void setContext(IHttpContext context) {
    this.context = context;
  }

	@Override
  public void load() {
  }
  
  private String getMimeTypeForFile() {
    String extension = Path.getFileExtensionFromFilename(getTemplate().getName());
    return getMimeTypeForExtension(extension);
  }
  
  private String getMimeTypeForExtension(String extension) {
    String mimeType = MimeTypeMap.getInstance().getMimeTypeForExtension(extension);
    if (mimeType == null) {
      mimeType = "application/octet-stream";
    }
    return mimeType;
  }

  @Override
  public String getContentType() {
    return getMimeTypeForFile();
  }

}
