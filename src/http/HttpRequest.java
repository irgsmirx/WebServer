package http;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

public class HttpRequest extends HttpMessage implements HttpConstants {
	protected int method;
	protected URI uri;
	protected int major;
	protected int minor;
	protected byte[] body;
	protected int length = -1;

	protected int ch = -1;

	protected Map<String, String> query = null;

	protected HttpHeaders headers = null;

	public static boolean isCHAR(int ch) {
		return (ch >= 0 && ch <= 127);
	}

	public static boolean isUPALPHA(int ch) {
		return (ch >= 65 && ch <= 90);
	}

	public static boolean isLOALPHA(int ch) {
		return (ch >= 97 && ch <= 122);
	}

	public static boolean isALPHA(int ch) {
		return (isUPALPHA(ch) || isLOALPHA(ch));
	}

	public static boolean isCTL(int ch) {
		return (ch >= 0 && ch <= 31 || ch == 127);
	}

	public static boolean isDIGIT(int ch) {
		return (ch >= 48 && ch <= 57);
	}

	public static boolean isCR(int ch) {
		return (ch == 13);
	}

	public static boolean isLF(int ch) {
		return (ch == 10);
	}

	public static boolean isSP(int ch) {
		return (ch == 32);
	}

	public static boolean isHT(int ch) {
		return (ch == 9);
	}

	public static boolean isDoubleQuoteMark(int ch) {
		return (ch == 34);
	}

	public static boolean isSeparator(int ch) {
		return (ch == '(' || ch == ')' || ch == '<' || ch == '>' || ch == '@' || ch == ',' || ch == ';' || ch == ':'
				|| ch == '\\' || ch == '\"' || ch == '/' || ch == '[' || ch == ']' || ch == '?' || ch == '=' || ch == '{'
				|| ch == '}' || ch == ' ' || ch == '\t');
	}

	public HttpRequest() {
		method = GET;
		uri = null;
		major = 1;
		minor = 1;
		body = null;

		query = null;

		headers = new HttpHeaders();
	}

	public HttpRequest(InputStream is) throws IOException, HttpException {
		parse(is);
	}

	public HttpRequest(int method, URI uri, int major, int minor, Map<String, String> general_header,
			Map<String, String> request_header, Map<String, String> entity_header) {
		this.method = method;
		this.uri = uri;
		this.major = major;
		this.minor = minor;

	}

	public HttpBuffer readRequestLine(InputStream is) throws IOException, HttpException {
		HttpBuffer buffer = new HttpBuffer();

		int last = -1;
		ch = is.read();

		while (isCR(ch) || isLF(ch)) {
			last = ch;
			ch = is.read();
		}

		while (true) {
			if (ch == -1) {
				// unepected end of input
				throw new HttpException(HTTP_BAD_REQUEST, "Your HTTP client's request ended unexpectedly.");
			} else if (ch == '\r') {
				last = ch;
			} else if (ch == '\n') {
				if (last == '\r') {
					break;
				} else {
					throw new HttpException(HTTP_BAD_REQUEST,
							"Your HTTP client's request contained an unallowed LF control character.");
				}
			} else {
				if (last == '\r') {
					throw new HttpException(HTTP_BAD_REQUEST,
							"Your HTTP client's request contained an unallowed LF control character.");
				} else {
					last = ch;
					buffer.append(ch);
				}
			}
			ch = is.read();
		}

		return buffer;
	}

	protected HttpBuffer readHeaderKey(InputStream is) throws IOException, HttpException {
		HttpBuffer buffer = new HttpBuffer();

		while (ch != ':') {
			if (ch == -1) {
				// unexpected end of input
				throw new HttpException(HTTP_BAD_REQUEST, "Your HTTP client's request ended unexpectedly.");
			} else if (isCTL(ch)) {
				// CTL character not allowed
				throw new HttpException(HTTP_BAD_REQUEST,
						"Your HTTP client's request header contained an unallowed CTL character: '" + (char) (ch & 0xff) + "'.");
			} else if (isSeparator(ch)) {
				// separator character not allowed
				throw new HttpException(HTTP_BAD_REQUEST,
						"Your HTTP client's request header contained an unallowed separator character: '" + (char) (ch & 0xff)
								+ "'.");
			} else {
				buffer.append(ch);
			}
			ch = is.read();
		}

		return buffer;
	}

	protected HttpBuffer readHeaderValue(InputStream is) throws IOException, HttpException {
		HttpBuffer buffer = new HttpBuffer();

		int last = -1;
		ch = is.read();

		boolean insideQuote = false;

		// skip LWS
		while (isSP(ch) || isHT(ch)) {
			last = ch;
			ch = is.read();
		}

		while (true) {
			if (ch == -1) {
				break;
			} else if (ch == '\r') {
				if (last == '\\') {
					if (insideQuote) {
						buffer.append(ch);
					}
				}
				last = ch;
			} else if (ch == '\n') {
				if (last == '\r') {
					if (insideQuote) {
						buffer.append('\r');
						buffer.append('\n');
					} else {
						ch = is.read();

						if (isSP(ch) || isHT(ch)) {
							// we are in a continuation line, skip LWS
							while (isSP(ch) || isHT(ch)) {
								last = ch;
								ch = is.read();
							}
							if (ch != '\r') {
								// append a single SP for LWS
								buffer.append(' ');
								buffer.append(ch);
							}
						} else {
							// finished reading value
							break;
						}
					}
				} else {
					if (insideQuote) {
						// LF character not allowed in quoted text
						throw new HttpException(HTTP_BAD_REQUEST,
								"Your HTTP client's request header contained an LF character in quoted text, which is not allowed.");
					}
				}
			} else if (ch == '\"') {
				if (last == '\\') {
					if (insideQuote) {
						buffer.append(ch);
					} else {
						insideQuote = !insideQuote;
					}
				} else {
					insideQuote = !insideQuote;
				}
			} else {
				last = ch;
				buffer.append(ch);
			}
			ch = is.read();
		}

		return buffer;
	}

	public void parseHeaders(InputStream is) throws IOException, HttpException {
		ch = is.read();

		while (true) {
			if (isCR(ch)) {
				ch = is.read();
				if (isLF(ch)) {
					return;
				} else {
					throw new HttpException(HTTP_BAD_REQUEST,
							"Your HTTP client's request contained an unallowed CR control character.");
				}
			}
			HttpBuffer keyBuffer = readHeaderKey(is);
			String key = keyBuffer.toString();
			HttpBuffer valueBuffer = readHeaderValue(is);
			String value = valueBuffer.toString();

			System.out.println(key + ", " + value);

			if (headers == null) {
				headers = new HttpHeaders();
			}
			headers.put(key, value);
		}
	}

	public void parse(InputStream is) throws IOException, HttpException {
		parseRequestLine(is);
		// parseHeaders(is);
		headers.parse(is);
		parseBody(is);
		System.out.println(body);
	}

	protected void parseRequestLine(InputStream is) throws IOException, HttpException {
		HttpBuffer requestLineBuffer = readRequestLine(is);
		String requestLine = requestLineBuffer.toString();

		String[] result = requestLine.split("\\s");

		if (result.length == 3) {
			String m = result[0].trim();
			String u = result[1].trim();
			String v = result[2].trim();

			if (m.compareTo("OPTIONS") == 0) {
				method = OPTIONS;
			} else if (m.compareTo("GET") == 0) {
				method = GET;
			} else if (m.compareTo("HEAD") == 0) {
				method = HEAD;
			} else if (m.compareTo("POST") == 0) {
				method = POST;
			} else if (m.compareTo("PUT") == 0) {
				method = PUT;
				throw new HttpException(HTTP_METHOD_NOT_ALLOWED, "Method '" + m + "' not allowed!");
			} else if (m.compareTo("DELETE") == 0) {
				method = DELETE;
				throw new HttpException(HTTP_METHOD_NOT_ALLOWED, "Method '" + m + "' not allowed!");
			} else if (m.compareTo("TRACE") == 0) {
				method = TRACE;
				throw new HttpException(HTTP_METHOD_NOT_ALLOWED, "Method '" + m + "' not allowed!");
			} else if (m.compareTo("CONNECT") == 0) {
				method = CONNECT;
				throw new HttpException(HTTP_METHOD_NOT_ALLOWED, "Method '" + m + "' not allowed!");
			} else {
				throw new HttpException(HTTP_NOT_IMPLEMENTED, "Method '" + m + "' not implemented!");
			}

			try {
				this.uri = new URI(u);
			} catch (URISyntaxException e) {
				throw new HttpException(HTTP_BAD_REQUEST, "URI '" + u + "' not valid!");
			}

			if (this.uri.getQuery() != null) {
				parseQuery(this.uri.getQuery());
			}

			if (v.compareTo(HttpUtils.httpVersion(1, 0)) == 0) {
				this.major = 1;
				this.minor = 0;
			} else if (v.compareTo(HttpUtils.httpVersion(1, 1)) == 0) {
				this.major = 1;
				this.minor = 1;
			} else {
				throw new HttpException(HTTP_VERSION_NOT_SUPPORTED, "Version '" + v + "' not supported!");
			}
		} else {
			throw new HttpException(HTTP_BAD_REQUEST, "Request line '" + requestLine + "' invalid!");
		}
	}

	protected Map<String, String> parseQuery(String query) throws HttpException {
		Map<String, String> result = null;

		if (query != null) {
			result = new HashMap<>();
			if (this.query == null) {
				this.query = new HashMap<>();
			}

			try {
				String[] pairs = query.split("\\&");

				for (int i = 0; i < pairs.length; i++) {
					String[] fields = pairs[i].split("=");

					if (fields.length == 2) {
						String key = URLDecoder.decode(fields[0], "UTF-8");
						String value = URLDecoder.decode(fields[1], "UTF-8");
						result.put(key, value);
						this.query.put(key, value);
					} else {
						throw new HttpException(HTTP_BAD_REQUEST, "Query part'" + pairs[i] + "' is not valid!");
					}
				}
			} catch (UnsupportedEncodingException e) {
				throw new HttpException(HTTP_INTERNAL_SERVER_ERROR, "An error occured decoding query '" + query + "'!");
			}
		} else {
			throw new HttpException(HTTP_INTERNAL_SERVER_ERROR, "An error occured decoding query '" + query + "'!");
		}

		return result;
	}

	protected void parseBody(InputStream is) throws IOException, HttpException {
		String contentLength = headers.get("Content-Length");
		String contentType = headers.get("Content-Type");
		String transferEncoding = headers.get("Transfer-Encoding");

		if (transferEncoding == null) {
			if (contentLength == null) {
				// well, no body present, as it seems
				return;
			} else {
				if (contentType.compareTo("application/x-www-form-urlencoded") == 0) {
					try {
						length = Integer.parseInt(contentLength);
						readPlain(is);
					} catch (NumberFormatException e) {
						throw new HttpException(HTTP_BAD_REQUEST, "Header field Content-Length contained invalid value '"
								+ contentLength + "'.");
					}
				} else {
					throw new HttpException(HTTP_UNSUPPORTED_MEDIA_TYPE, "Media type is not supported.");
				}
			}
		} else {
			if (transferEncoding.compareTo("chunked") == 0) {
				readChunked(is);
			} else {
				throw new HttpException(HTTP_NOT_IMPLEMENTED, "The Transfer-Encoding '" + transferEncoding
						+ "' is not supported.");
			}
		}
	}

	protected void readPlain(InputStream is) throws IOException, HttpException {
		HttpBuffer buffer = new HttpBuffer();
		int written = 1;

		ch = is.read();

		while (written < length) {
			if (ch == -1) {
				throw new HttpException(HTTP_BAD_REQUEST, "Unexpected end of stream.");
			} else {
				buffer.append(ch);
				written++;
			}
			ch = is.read();
		}

		body = buffer.getCopy();
	}

	protected void readChunked(InputStream is) throws IOException, HttpException {
		HttpBuffer buffer = new HttpBuffer();
		ChunkedInputStream cis = new ChunkedInputStream(is, headers);

		int ch = cis.read();

		while (true) {
			if (ch == -1) {
				break;
			} else {
				buffer.append(ch);
			}
		}

		body = buffer.getCopy();
	}

	protected static int contains(String[] array, String value) {
		for (int i = 0; i < array.length; i++) {
			if (array[i].compareTo(value) == 0) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * @return Returns the entity_header.
	 */
	public Map<String, String> getEntityHeader() {
		return entityHeader;
	}

	/**
	 * @param entity_header
	 *          The entity_header to set.
	 */
	public void setEntityHeader(Map<String, String> entityHeader) {
		this.entityHeader = entityHeader;
	}

	/**
	 * @return Returns the general_header.
	 */
	public Map<String, String> getGeneralHeader() {
		return generalHeader;
	}

	/**
	 * @param general_header
	 *          The general_header to set.
	 */
	public void setGeneralHeader(Map<String, String> generalHeader) {
		this.generalHeader = generalHeader;
	}

	/**
	 * @return Returns the header.
	 */
	public Map<String, String> getHeader() {
		return header;
	}

	/**
	 * @param header
	 *          The header to set.
	 */
	public void setHeader(Map<String, String> header) {
		this.header = header;
	}

	/**
	 * @return Returns the method.
	 */
	public int getMethod() {
		return method;
	}

	/**
	 * @param method
	 *          The method to set.
	 */
	public void setMethod(int method) {
		this.method = method;
	}

	/**
	 * @return Returns the query.
	 */
	public Map<String, String> getQuery() {
		return query;
	}

	/**
	 * @param query
	 *          The query to set.
	 */
	public void setQuery(Map<String, String> query) {
		this.query = query;
	}

	/**
	 * @return Returns the uri.
	 */
	public URI getUri() {
		return uri;
	}

	/**
	 * @param uri
	 *          The uri to set.
	 */
	public void setUri(URI uri) {
		this.uri = uri;
	}

	/**
	 * @return Returns the major.
	 */
	public int getMajor() {
		return major;
	}
	/**
	 * @param version
	 *          The major to set.
	 */
	public void setMajor(int major) {
		this.major = major;
	}

	/**
	 * @return Returns the minor.
	 */
	public int getMinor() {
		return minor;
	}

	/**
	 * @param minor
	 *          The minor to set.
	 */
	public void setMinor(int minor) {
		this.minor = minor;
	}

	/**
	 * @return Returns the body.
	 */
	public byte[] getBody() {
		return body;
	}

	/**
	 * @param body
	 *          The body to set.
	 */
	public void setBody(byte[] body) {
		this.body = body;
	}

	/**
	 * @return Returns the headers.
	 */
	public HttpHeaders getHeaders() {
		return headers;
	}

	/**
	 * @param headers
	 *          The headers to set.
	 */
	public void setHeaders(HttpHeaders headers) {
		this.headers = headers;
	}

	/**
	 * @return Returns the length.
	 */
	public int getLength() {
		return length;
	}

	/**
	 * @param length
	 *          The length to set.
	 */
	public void setLength(int length) {
		this.length = length;
	}

}
