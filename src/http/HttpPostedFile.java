/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package http;

import java.io.InputStream;

/**
 *
 * @author tobias
 */
public class HttpPostedFile {
	
	private int contentLength;
	private String contentType;
	private String fileName;
	
	public int getContentLength() {
		return contentLength;
	}
	
	public String getContentType() {
		return contentType;
	}
	
	public String getFileName() {
		return fileName;
	}
	
	public InputStream getInputStream() {
		return null;
	}
	
	public void saveAs(String filename) {
		
	}
	
}
