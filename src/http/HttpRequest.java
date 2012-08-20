package http;

import exceptions.HttpException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

public class HttpRequest extends HttpMessage implements IHttpRequest {
	
  private InputStream inputStream;
  
  protected HttpMethod method;
	protected URI uri;
	
	protected HttpBrowserCapabilities browser;
	
	protected String contentEncoding;
	protected String contentType;

  protected byte[] body;

	protected Map<String, String> queryString = null;
	protected Map<String, String> form = null;
	protected Map<String, String> serverVariables = null;

	protected URI urlReferrer;
	
  protected IHttpVersion version;
  
	protected String userAgent;
	protected String userHostAddress;
	protected String userHostName;
	protected String[] userLanguages;

	public HttpRequest() {
		method = HttpMethod.GET;
		uri = null;

		queryString = null;

		headers = new HttpHeaders();
	}

	public HttpRequest(InputStream is) throws IOException, HttpException {
		HttpParser parser = new HttpParser();
    this.headers = parser.parseHeaders(is);
	}

	public HttpRequest(IHttpHeaders httpHeaders) {
		this.headers = httpHeaders;
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
	 * @return Returns the method.
	 */
	@Override
	public HttpMethod getMethod() {
		return method;
	}

	/**
	 * @param method
	 *          The method to set.
	 */
	@Override
	public void setMethod(HttpMethod value) {
		this.method = value;
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
  @Override
	public URI getUri() {
		return uri;
	}

	/**
	 * @param uri
	 *          The uri to set.
	 */
  @Override
	public void setUri(URI uri) {
		this.uri = uri;
	}

  @Override
  public IHttpVersion getVersion() {
    return version;
  }
          
  @Override
  public void setVersion(IHttpVersion value) {
    this.version = value;
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
	
  @Override
  public InputStream getInputStream() {
    return inputStream;
  }
  
  @Override
  public void setInputStream(InputStream value) {
    this.inputStream = value;
  }
  
}
