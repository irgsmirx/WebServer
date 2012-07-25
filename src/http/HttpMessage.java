package http;

import java.util.Map;

public abstract class HttpMessage extends HttpCodes implements HttpConstants, HttpMethods {

	public static int BUF_SIZE = 2048;

	public static final String SP = " ";
	public static final String CR = "\r";
	public static final String LF = "\n";
	public static final String EOL = CR + LF;

	public static final String HTTP10 = "HTTP/1.0";
	public static final String HTTP11 = "HTTP/1.1";

	public static final String[] METHODS = {"OPTIONS", "GET", "HEAD", "POST", "PUT", "DELETE", "TRACE", "CONNECT"};

	public static final String[] general_header_keys = {"Cache-Control", "Connection", "Date", "Pragma", "Trailer",
			"Transfer-Encoding", "Upgrade", "Via", "Warning"};
	public static final String[] request_header_keys = {"Accept", "Accept-Charset", "Accept-Encoding", "Accept-Language",
			"Authorization", "Expect", "From", "Host", "If-Match", "If-Modified-Since", "If-None-Match", "If-Range",
			"If-Unmodified-Since", "Max-Forwards", "Proxy-Authorization", "Range", "Referer", "TE", "User-Agent"};
	public static final String[] response_header_keys = {"Accept-Ranges", "Age", "ETag", "Location",
			"Proxy-Auhtenticate", "Retry-After", "Server", "Vary", "WWW-Authenticate"};
	public static final String[] entity_header_keys = {"Allow", "Content-Encoding", "Content-Language", "Content-Length",
			"Content-Location", "Content-MD5", "Content-Range", "Content-Type", "Expires", "Last-Modified"};

	protected static final int REQUESTLINE = 0;
	protected static final int HEADERLINES = 1;
	protected static final int ENTITYBODY = 2;

	protected Map<String, String> generalHeader;
	protected Map<String, String> entityHeader;

	protected Map<String, String> header;

	protected static void clearBuffer(byte[] buf) {
		for (int i = 0; i < BUF_SIZE; i++) {
			buf[i] = 0;
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
