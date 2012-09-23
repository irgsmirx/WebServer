package com.ramforth.webserver.http;

import java.io.IOException;
import java.io.PrintStream;

public class XHTMLContainer implements WebContainer {

	//private XHTMLDocument document;
	private String contentType = "text/html";
	private String filename;

	/**
	 * @return Returns the filename.
	 */
	public String getFilename() {
		return filename;
	}

	/**
	 * @param filename
	 *          The filename to set.
	 */
	public void setFilename(String filename) {
		this.filename = filename;
	}

	public XHTMLContainer() {
	}

  @Override
	public void print(PrintStream p) throws IOException {
		//if (document != null) {
		//	document.printXML(p);
		//}
	}

  @Override
	public boolean isConsistent() {
		//return (document != null);
    return false;
	}

	/**
	 * @return Returns the document.
	 */
//	public XHTMLDocument getDocument() {
//		return document;
//	}

	/**
	 * @param document
	 *          The document to set.
	 */
//	public void setDocument(XHTMLDocument document) {
//		this.document = document;
//	}

	/**
	 * @return Returns the contentType.
	 */
  @Override
	public String getContentType() {
		return contentType;
	}

	/**
	 * @param contentType
	 *          The contentType to set.
	 */
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

  @Override
	public long getContentLength() {
		StringBuilder sb = new StringBuilder();
		//document.appendXML(sb);

		return sb.length();
	}

}
