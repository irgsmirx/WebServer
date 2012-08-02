package http;

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

	public void print(PrintStream p) throws IOException {
		//if (document != null) {
		//	document.printXML(p);
		//}
	}

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

	public long getContentLength() {
		StringBuffer sb = new StringBuffer();
		//document.appendXML(sb);

		return sb.length();
	}

}
