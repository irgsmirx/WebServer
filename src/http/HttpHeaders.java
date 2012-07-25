package http;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Enumeration;
import java.util.Hashtable;

public class HttpHeaders extends HttpUtils implements HttpConstants {

	protected Hashtable<String, String> headers = null;
	protected int ch = -1;

	public HttpHeaders() {
	}

	public void addHeader(String key, String value) {
		String lowerKey = key.toLowerCase();
		String currentValue = null;

		if (headers == null) {
			headers = new Hashtable<String, String>();
		} else {
			currentValue = headers.get(lowerKey);
		}

		if (lowerKey == null) {
			headers.put(lowerKey, value);
		} else {
			headers.put(lowerKey, currentValue + "," + value);
		}
	}

	public String put(String key, String value) {
		if (headers == null) {
			headers = new Hashtable<String, String>();
		}
		return headers.put(key.toLowerCase(), value);
	}

	public String get(String key) {
		if (headers != null) {
			return headers.get(key.toLowerCase());
		} else {
			return null;
		}
	}

	public Enumeration<String> elements() {
		if (headers != null) {
			return headers.elements();
		} else {
			return null;
		}
	}

	public String toString() {
		String result = null;
		Enumeration<String> keys = elements();
		if (keys != null) {
			result = "";
			while (keys.hasMoreElements()) {
				String key = keys.nextElement();
				String value = headers.get(key);
				result += key + ": " + value + "\n";
				System.out.println(key + ": " + value);
			}
		}
		return result;
	}

	public void print(PrintStream ps) {
		Enumeration<String> keys = elements();
		if (keys != null) {
			while (keys.hasMoreElements()) {
				String key = keys.nextElement();
				String value = headers.get(key);
				ps.println(key + ": " + value);
			}
		}
	}

	public void parse(InputStream is) throws IOException, HttpException {
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

			put(key, value);
		}
	}

	public HttpBuffer readHeaderKey(InputStream is) throws IOException, HttpException {
		HttpBuffer buffer = new HttpBuffer();

		while (ch != ':') {
			if (ch == -1) {
				// unexpected end of input
				throw new HttpException(HTTP_BAD_REQUEST, "Your HTTP client's request ended unexpectedly.");
			} else if (isCTL(ch)) {
				// CTL character not allowed
				throw new HttpException(HTTP_BAD_REQUEST,
						"Your HTTP client's request header contained an unallowed CTL character: '" + ch + "'.");
			} else if (isSeparator(ch)) {
				// separator character not allowed
				throw new HttpException(HTTP_BAD_REQUEST,
						"Your HTTP client's request header contained an unallowed separator character: '" + ch + "'.");
			} else {
				buffer.append(ch);
			}
			ch = is.read();
		}

		return buffer;
	}

	public HttpBuffer readHeaderValue(InputStream is) throws IOException, HttpException {
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

}
