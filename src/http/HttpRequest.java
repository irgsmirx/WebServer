package http;

import exceptions.HttpException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

public class HttpRequest extends HttpMessage implements IHttpRequest {
	protected HttpMethod method;
	protected URI uri;
	protected int major;
	protected int minor;
	protected byte[] body;
	
	protected HttpBrowserCapabilities browser;
	
	protected String contentEncoding;
	protected int contentLength = -1;
	protected String contentType;

	protected int ch = -1;

	protected Map<String, String> queryString = null;
	protected Map<String, String> form = null;
	protected Map<String, String> serverVariables = null;

	protected URI urlReferrer;
	
	protected String userAgent;
	protected String userHostAddress;
	protected String userHostName;
	protected String[] userLanguages;

	public HttpRequest() {
		method = HttpMethod.GET;
		uri = null;
		major = 1;
		minor = 1;
		body = null;

		queryString = null;

		headers = new HttpHeaders();
	}

	public HttpRequest(InputStream is) throws IOException, HttpException {
		parse(is);
	}

	public HttpRequest(HttpMethod method, URI uri, int major, int minor, Map<String, String> general_header,
			Map<String, String> request_header, Map<String, String> entity_header) {
		this.method = method;
		this.uri = uri;
		this.major = major;
		this.minor = minor;

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

	protected Map<String, String> parseQuery(String query) throws HttpException {
		Map<String, String> result = null;

		if (query != null) {
			result = new HashMap<>();
			if (this.queryString == null) {
				this.queryString = new HashMap<>();
			}

			try {
				String[] pairs = query.split("\\&");

				for (int i = 0; i < pairs.length; i++) {
					String[] fields = pairs[i].split("=");

					if (fields.length == 2) {
						String key = URLDecoder.decode(fields[0], "UTF-8");
						String value = URLDecoder.decode(fields[1], "UTF-8");
						result.put(key, value);
						this.queryString.put(key, value);
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
						this.contentLength = Integer.parseInt(contentLength);
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

		while (written < contentLength) {
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
	public Map<String, String> getQueryString() {
		return queryString;
	}

  public String getQueryString(String key) {
    return queryString.get(key);
  }
  
	/**
	 * @param query
	 *          The query to set.
	 */
	public void setQueryString(Map<String, String> query) {
		this.queryString = query;
	}

  public void setQueryString(String key, String value) {
    this.queryString.put(key, value);
  }
  
	/**	 * @return Returns the uri.
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
  @Override
	public IHttpHeaders getHeaders() {
		return headers;
	}

	/**
	 * @param headers
	 *          The headers to set.
	 */
	public void setHeaders(IHttpHeaders headers) {
		this.headers = headers;
	}

	public HttpBrowserCapabilities getBrowser() {
		return browser;
	}
	
	public void setBrowser(HttpBrowserCapabilities value) {
		this.browser = value;
	}
	
	public String getItem(String key) {
		String value = queryString.get(key);
		
		if (value == null) {
			value = form.get(key);
		}
		
		if (value == null) {
			HttpCookie cookie = cookies.get(key);
			if (cookie != null) {
				value = cookie.getValue();
			}
		}
		
		if (value == null) {
			value = serverVariables.get(key);
		}

		return value;
	}
	
	public Map<String, String> getParams() {
		Map<String, String> params = new HashMap<>();
		params.putAll(queryString);
		params.putAll(form);
		
		for (HttpCookie cookie : cookies.values()) {
			params.put(cookie.getName(), cookie.getValue());
		}
		
		params.putAll(serverVariables);
		
		return params;
	}
	
	public int getContentLength() {
		return contentLength;
	}
	
	public String getContentType() {
		return contentType;
	}
	
	public void setContentType(String value) {
		this.contentType = value;
	}
	
	public Map<String, HttpPostedFile> getFiles() {
		return null;
	}

	public URI getUrlReferrer() {
		return urlReferrer;
	}
	
	public String getUserAgent() {
		return userAgent;
	}
	
	public String getUserHostAddress() {
		return userHostAddress;
	}
	
	public String getUserHostName() {
		return userHostName;
	}
	
	public String[] getUserLanguages() {
		return userLanguages;
	}
	
}
