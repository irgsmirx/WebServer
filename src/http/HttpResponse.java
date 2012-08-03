package http;

import java.io.File;
import java.io.PrintStream;
import java.net.SocketTimeoutException;
import java.text.SimpleDateFormat;
import java.util.Map.Entry;
import java.util.*;

public class HttpResponse extends HttpMessage implements IHttpResponse {

	protected String version;
	protected int status;
	protected String reason;
	protected int contentLength;
	protected String body;

	protected Map<String, String> responseHeader;

	public HttpResponse() {
		version = null;
		status = -1;
		reason = null;
		contentLength = 0;
		body = null;

		header = null;

		generalHeader = new HashMap<>();
		responseHeader = new HashMap<>();
		entityHeader = new HashMap<>();
	}

	public HttpResponse(SocketTimeoutException e) {
		this(new HttpException(HTTP_REQUEST_TIMEOUT));
	}

	public HttpResponse(HttpException e) {
		version = HTTP11;
		status = e.getErrorCode();
		reason = get(status);

		generalHeader = new HashMap<>();
		responseHeader = new HashMap<>();
		entityHeader = new HashMap<>();

		String title = "ERROR " + status + " (" + reason + ")";

		//XHTMLDocument document = errorDocument(e, title);

		StringBuffer sb = new StringBuffer();
		//document.appendXML(sb);

		body = sb.toString();

		contentLength = sb.length();

		generateDefaultHeader();
	}

	public HttpResponse(File f) {
		if (f.exists()) {
			if (f.canRead()) {
				if (f.isFile()) {

				} else if (f.isDirectory()) {
					version = HTTP11;
					status = HTTP_OK;
					reason = get(status);

					generalHeader = new HashMap<>();
					responseHeader = new HashMap<>();
					entityHeader = new HashMap<>();

					//XHTMLDocument document = directoryListing(f);

					StringBuffer sb = new StringBuffer();
					//document.appendXML(sb);

					body = sb.toString();

					contentLength = sb.length();

					generateDefaultHeader();
				} else {

				}
			} else {
				version = HTTP11;
				status = HTTP_FORBIDDEN;
				reason = get(status);

				generalHeader = new HashMap<>();
				responseHeader = new HashMap<>();
				entityHeader = new HashMap<>();

				String title = "ERROR " + status + " (" + reason + ")";

				//XHTMLDocument document = accessDenied(f, title);

				StringBuffer sb = new StringBuffer();
				//document.appendXML(sb);

				body = sb.toString();

				contentLength = sb.length();

				generateDefaultHeader();
			}
		} else {
			version = HTTP11;
			status = HTTP_NOT_FOUND;
			reason = get(status);

			generalHeader = new HashMap<>();
			responseHeader = new HashMap<>();
			entityHeader = new HashMap<>();

			String title = "ERROR " + status + " (" + reason + ")";

			//XHTMLDocument document = fileNotFound(f, title);

			StringBuffer sb = new StringBuffer();
			//document.appendXML(sb);

			body = sb.toString();

			contentLength = sb.length();

			generateDefaultHeader();
		}
	}

	public void print(PrintStream ps) {
		String statusline = version + " " + status + " " + reason;
		ps.print(statusline + EOL);

		printHashtable(ps, generalHeader);
		printHashtable(ps, responseHeader);
		printHashtable(ps, entityHeader);

		ps.print(EOL);

		if (body != null) {
			ps.print(body);
		}
	}

	protected void generateDefaultHeader() {
		SimpleDateFormat formatter = new SimpleDateFormat("EEE, dd MMM yyyy hh:mm:ss zzz", Locale.US);
		formatter.setTimeZone(TimeZone.getTimeZone("GMT"));
		Date date = new Date();

		generalHeader.put("Date", formatter.format(date));
		generalHeader.put("Connection", "close");
		//responseHeader.put("Server", WebServer.serverid);
		entityHeader.put("Content-Length", String.valueOf(contentLength));
		entityHeader.put("Content-Type", "text/html");
	}

//	protected XHTMLDocument errorDocument(Exception e, String title) {
//		XHTMLHeading1 heading = new XHTMLHeading1();
//		XHTMLPCData pcdata = new XHTMLPCData(title);
//		heading.add(pcdata);
//
//		XHTMLParagraph paragraph = new XHTMLParagraph();
//		XHTMLPCData pcdata2 = new XHTMLPCData(e.getMessage());
//		paragraph.add(pcdata2);
//
//		XHTMLBody body = new XHTMLBody();
//		body.add(heading);
//		body.add(paragraph);
//		XHTMLDocument document = new XHTMLDocument(title);
//		document.setBody(body);
//
//		return document;
//	}
//
//	protected XHTMLDocument fileNotFound(File f, String title) {
//		XHTMLHeading1 heading = new XHTMLHeading1();
//		XHTMLPCData pcdata = new XHTMLPCData(title);
//		heading.add(pcdata);
//
//		XHTMLParagraph paragraph = new XHTMLParagraph();
//		XHTMLPCData pcdata2 = new XHTMLPCData("'" + f.getAbsolutePath() + "' could not be found.");
//		paragraph.add(pcdata2);
//
//		XHTMLBody body = new XHTMLBody();
//		body.add(heading);
//		body.add(paragraph);
//		XHTMLDocument document = new XHTMLDocument(title);
//		document.setBody(body);
//
//		return document;
//	}
//
//	protected XHTMLDocument accessDenied(File f, String title) {
//		XHTMLHeading1 heading = new XHTMLHeading1();
//		XHTMLPCData pcdata = new XHTMLPCData(title);
//		heading.add(pcdata);
//
//		XHTMLParagraph paragraph = new XHTMLParagraph();
//		XHTMLPCData pcdata2 = new XHTMLPCData("You do not have the permission to access '" + f.getAbsolutePath() + "'!");
//		paragraph.add(pcdata2);
//
//		XHTMLBody body = new XHTMLBody();
//		body.add(heading);
//		body.add(paragraph);
//		XHTMLDocument document = new XHTMLDocument(title);
//		document.setBody(body);
//
//		return document;
//	}
//
//	protected XHTMLDocument directoryListing(File dir) {
//		XHTMLHeading1 heading = new XHTMLHeading1();
//		XHTMLPCData pcdata = new XHTMLPCData("Index of " + dir.getAbsolutePath());
//		heading.add(pcdata);
//
//		XHTMLTable table = new XHTMLTable();
//		XHTMLTableHeader thead = new XHTMLTableHeader();
//
//		XHTMLTableRow trHead = new XHTMLTableRow();
//		XHTMLTableHead thFileName = new XHTMLTableHead();
//		XHTMLPCData headFileName = new XHTMLPCData("Name");
//		thFileName.add(headFileName);
//		trHead.add(thFileName);
//		XHTMLTableHead thLastModfied = new XHTMLTableHead();
//		XHTMLPCData headLastModified = new XHTMLPCData("Last modified");
//		thLastModfied.add(headLastModified);
//		trHead.add(thLastModfied);
//		XHTMLTableHead thSize = new XHTMLTableHead();
//		XHTMLPCData headSize = new XHTMLPCData("Size");
//		thSize.add(headSize);
//		trHead.add(thSize);
//
//		thead.add(trHead);
//		table.setThead(thead);
//
//		XHTMLTableBody tbody = new XHTMLTableBody();
//
//		XHTMLTableRow tr1 = new XHTMLTableRow();
//		XHTMLTableData td11 = new XHTMLTableData();
//		XHTMLAnchor anchor11 = new XHTMLAnchor();
//		anchor11.setHref("../");
//		anchor11.setName("Parent_directory");
//		XHTMLPCData pcdata11 = new XHTMLPCData("../");
//		anchor11.add(pcdata11);
//		td11.add(anchor11);
//		tr1.add(td11);
//		XHTMLTableData td12 = new XHTMLTableData();
//		XHTMLPCData pcdata12 = new XHTMLPCData(new Date(dir.lastModified()).toString());
//		td12.add(pcdata12);
//		tr1.add(td12);
//		XHTMLTableData td13 = new XHTMLTableData();
//		XHTMLPCData pcdata13 = new XHTMLPCData("-");
//		td13.add(pcdata13);
//		tr1.add(td13);
//		tbody.add(tr1);
//
//		String[] list = dir.list();
//		for (int i = 0; list != null && i < list.length; i++) {
//			File f = new File(dir, list[i]);
//			if (f.isDirectory()) {
//				XHTMLTableRow tr2 = new XHTMLTableRow();
//				XHTMLTableData td21 = new XHTMLTableData();
//				XHTMLAnchor anchor21 = new XHTMLAnchor();
//				anchor21.setHref(list[i] + "/");
//				anchor21.setName(list[i]);
//				XHTMLPCData pcdata21 = new XHTMLPCData(list[i] + "/");
//				anchor21.add(pcdata21);
//				td21.add(anchor21);
//				tr2.add(td21);
//				XHTMLTableData td22 = new XHTMLTableData();
//				XHTMLPCData pcdata22 = new XHTMLPCData(new Date(dir.lastModified()).toString());
//				td22.add(pcdata22);
//				tr2.add(td22);
//				XHTMLTableData td23 = new XHTMLTableData();
//				XHTMLPCData pcdata23 = new XHTMLPCData("-");
//				td23.add(pcdata23);
//				tr2.add(td23);
//				tbody.add(tr2);
//			} else {
//				XHTMLTableRow tr2 = new XHTMLTableRow();
//				XHTMLTableData td21 = new XHTMLTableData();
//				XHTMLAnchor anchor21 = new XHTMLAnchor();
//				anchor21.setHref(list[i]);
//				anchor21.setName(list[i]);
//				XHTMLPCData pcdata21 = new XHTMLPCData(list[i]);
//				anchor21.add(pcdata21);
//				td21.add(anchor21);
//				tr2.add(td21);
//				XHTMLTableData td22 = new XHTMLTableData();
//				XHTMLPCData pcdata22 = new XHTMLPCData(new Date(dir.lastModified()).toString());
//				td22.add(pcdata22);
//				tr2.add(td22);
//				XHTMLTableData td23 = new XHTMLTableData();
//				XHTMLPCData pcdata23 = new XHTMLPCData("-");
//				td23.add(pcdata23);
//				tr2.add(td23);
//				tbody.add(tr2);
//			}
//		}
//
//		table.setTbody(tbody);
//
//		XHTMLHorizontalLine hl = new XHTMLHorizontalLine();
//		XHTMLParagraph paragraph2 = new XHTMLParagraph();
//		XHTMLItalic italic = new XHTMLItalic();
//		XHTMLPCData pcdata4 = new XHTMLPCData(WebServer.serverid + " " + new Date());
//		italic.add(pcdata4);
//		paragraph2.add(italic);
//
//		XHTMLBody body = new XHTMLBody();
//		body.add(heading);
//		body.add(table);
//		body.add(hl);
//		body.add(paragraph2);
//		XHTMLDocument document = new XHTMLDocument("Index of " + dir.getAbsolutePath());
//		document.setBody(body);
//
//		return document;
//	}

	protected void printHashtable(PrintStream ps, Map<String, String> ht) {
		Set<Entry<String, String>> entrySet = ht.entrySet();
		Iterator<Entry<String, String>> i = entrySet.iterator();
		while (i.hasNext()) {
			Entry<String, String> next = i.next();
			ps.print(next.getKey() + ": " + next.getValue() + EOL);
		}
	}

	protected static int contains(String[] array, String value) {
		for (int i = 0; i < array.length; i++) {
			if (array[i].compareTo(value) == 0) {
				return i;
			}
		}
		return -1;
	}

}
