/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package http;

import exceptions.HttpException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Tobias Ramforth <tobias.ramforth at tu-dortmund.de>
 */
public class HttpParser implements IHttpParser {

  @Override
  public IHttpHeader parseHeader(InputStream is) {
    throw new UnsupportedOperationException("Not supported yet.");
  }
  
  @Override
  public IHttpRequestLine parseRequestLine(InputStream is) throws IOException, HttpException {
		HttpBuffer requestLineBuffer = readRequestLine(is);
		String requestLine = requestLineBuffer.toString();

		String[] result = requestLine.split("\\s");

    HttpMethod method;
    IHttpVersion version;
    URI uri;
    
		if (result.length == 3) {
			String m = result[0].trim();
			String u = result[1].trim();
			String v = result[2].trim();

			if (m.compareTo("OPTIONS") == 0) {
				method = HttpMethod.OPTIONS;
			} else if (m.compareTo("GET") == 0) {
				method = HttpMethod.GET;
			} else if (m.compareTo("HEAD") == 0) {
				method = HttpMethod.HEAD;
			} else if (m.compareTo("POST") == 0) {
				method = HttpMethod.POST;
			} else if (m.compareTo("PUT") == 0) {
				method = HttpMethod.PUT;
				throw new HttpException(HttpError.METHOD_NOT_ALLOWED, "Method '" + m + "' not allowed!");
			} else if (m.compareTo("DELETE") == 0) {
				method = HttpMethod.DELETE;
				throw new HttpException(HttpError.METHOD_NOT_ALLOWED, "Method '" + m + "' not allowed!");
			} else if (m.compareTo("TRACE") == 0) {
				method = HttpMethod.TRACE;
				throw new HttpException(HttpError.METHOD_NOT_ALLOWED, "Method '" + m + "' not allowed!");
			} else if (m.compareTo("CONNECT") == 0) {
				method = HttpMethod.CONNECT;
				throw new HttpException(HttpError.METHOD_NOT_ALLOWED, "Method '" + m + "' not allowed!");
			} else {
				throw new HttpException(HttpError.NOT_IMPLEMENTED, "Method '" + m + "' not implemented!");
			}

			try {
				uri = new URI(u);
			} catch (URISyntaxException e) {
				throw new HttpException(HttpError.BAD_REQUEST, "URI '" + u + "' not valid!");
			}

			if (uri.getQuery() != null) {
				parseQuery(uri.getQuery());
			}

			if (v.compareTo(HttpUtils.httpVersion(1, 0)) == 0) {
				version = new HttpVersion(1, 0);
			} else if (v.compareTo(HttpUtils.httpVersion(1, 1)) == 0) {
        version = new HttpVersion(1, 1);
			} else {
				throw new HttpException(HttpError.VERSION_NOT_SUPPORTED, "Version '" + v + "' not supported!");
			}
		} else {
			throw new HttpException(HttpError.BAD_REQUEST, "Request line '" + requestLine + "' invalid!");
		}
    
    return new HttpRequestLine(method, version);
	}
  
 	public HttpBuffer readRequestLine(InputStream is) throws IOException, HttpException {
		HttpBuffer buffer = new HttpBuffer();

		int last = -1;
		int ch = -1;
    ch = is.read();

		while (isCR(ch) || isLF(ch)) {
			last = ch;
			ch = is.read();
		}

		while (true) {
			if (ch == -1) {
				// unepected end of input
				throw new HttpException(HttpConstants.HTTP_BAD_REQUEST, "Your HTTP client's request ended unexpectedly.");
			} else if (ch == '\r') {
				last = ch;
			} else if (ch == '\n') {
				if (last == '\r') {
					break;
				} else {
					throw new HttpException(HttpConstants.HTTP_BAD_REQUEST,
							"Your HTTP client's request contained an unallowed LF control character.");
				}
			} else {
				if (last == '\r') {
					throw new HttpException(HttpConstants.HTTP_BAD_REQUEST,
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

    int ch = -1;
    
		while (ch != ':') {
			if (ch == -1) {
				// unexpected end of input
				throw new HttpException(HttpError.BAD_REQUEST, "Your HTTP client's request ended unexpectedly.");
			} else if (isCTL(ch)) {
				// CTL character not allowed
				throw new HttpException(HttpError.BAD_REQUEST,
						"Your HTTP client's request header contained an unallowed CTL character: '" + (char) (ch & 0xff) + "'.");
			} else if (isSeparator(ch)) {
				// separator character not allowed
				throw new HttpException(HttpError.BAD_REQUEST,
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
    int ch = -1;

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
						throw new HttpException(HttpError.BAD_REQUEST,
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
  
  protected Map<String, String> parseQuery(String query) throws HttpException {
		Map<String, String> result = null;

		if (query != null) {
			result = new HashMap<>();

			try {
				String[] pairs = query.split("\\&");

				for (int i = 0; i < pairs.length; i++) {
					String[] fields = pairs[i].split("=");

					if (fields.length == 2) {
						String key = URLDecoder.decode(fields[0], "UTF-8");
						String value = URLDecoder.decode(fields[1], "UTF-8");
						result.put(key, value);
					} else {
						throw new HttpException(HttpError.BAD_REQUEST, "Query part'" + pairs[i] + "' is not valid!");
					}
				}
			} catch (UnsupportedEncodingException e) {
				throw new HttpException(HttpError.INTERNAL_SERVER_ERROR, "An error occured decoding query '" + query + "'!");
			}
		} else {
			throw new HttpException(HttpError.INTERNAL_SERVER_ERROR, "An error occured decoding query '" + query + "'!");
		}

		return result;
	}

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
  
}
