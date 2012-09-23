package com.ramforth.webserver.http;

import com.ramforth.webserver.http.headers.entity.ContentTypeHttpHeader;
import com.ramforth.webserver.http.headers.entity.ContentLengthHttpHeader;
import java.util.Map;

public abstract class HttpMessage extends HttpCodes implements HttpConstants, IHttpMessage {

	public static int BUF_SIZE = 2048;

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

  protected Map<String, HttpCookie> cookies = null;

  protected IHttpVersion version = HttpVersion.HTTP_11;
  protected IHttpHeaders headers = new HttpHeaders();

  protected String contentType;
  protected long contentLength;
  
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

  @Override
  public IHttpVersion getVersion() {
    return version;
  }
  
  @Override
  public void setVersion(IHttpVersion value) {
    this.version = value;
  }
  
  @Override
  public IHttpHeaders getHeaders() {
    return headers;
  }
  
  @Override
  public long getContentLength() {
    return contentLength;
  }

  @Override
  public void setContentLength(long value) {
    this.contentLength = value;
    if (headers.contains("Content-Length")) {
      headers.getHeader("Content-Length").setRawValue(String.valueOf(value));
    } else {
      headers.addHeader(new ContentLengthHttpHeader(value));
    }
  }
  
  @Override
  public String getContentType() {
    return contentType;
  }

  @Override
  public void setContentType(String value) {
    this.contentType = value;
    if (headers.contains("Content-Type")) {
      headers.getHeader("Content-Type").setRawValue(value);
    } else {
      headers.addHeader(new ContentTypeHttpHeader(value));
    }
  }
  
}
