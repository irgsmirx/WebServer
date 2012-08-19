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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Tobias Ramforth <tobias.ramforth at tu-dortmund.de>
 */
public class HttpParser implements IHttpParser {

  private IHttpHeaderFactory httpHeaderFactory = new HttpHeaderFactory();
  
  @Override
  public IHttpRequest parseRequest(InputStream is) {
    IHttpRequest httpRequest = new HttpRequest();
		
    IHttpRequestLine requestLine;
    requestLine = parseRequestLine(is);
    
    try {
      IHttpHeaders httpHeaders = parseHeaders(is);
      parseBody(is, httpHeaders);
    } catch (Exception ex) {
      
    }

    if (requestLine.getVersion().isHTTP11() && !httpRequest.getHeaders().contains("host")) {
      //throw new HttpException(HttpStatusCode.STATUS_400_BAD_REQUEST, "HTTP/1.1 request without host header.");
    }
    
    httpRequest.setVersion(requestLine.getVersion());
    httpRequest.setUri(requestLine.getURI());
    
    return httpRequest;
  }
  
  @Override
  public IHttpHeaders parseHeaders(InputStream is) {
    IHttpHeaders httpHeaders = new HttpHeaders();
    
    int ch = -1;
    try {
      ch = is.read();
    } catch (IOException ex) {
      Logger.getLogger(HttpParser.class.getName()).log(Level.SEVERE, null, ex);
      throw new exceptions.IOException(ex);
    }

		while (true) {
			if (isCR(ch)) {
        try {
          ch = is.read();
        } catch (IOException ex) {
          Logger.getLogger(HttpParser.class.getName()).log(Level.SEVERE, null, ex);
          throw new exceptions.IOException(ex);
        }
        
				if (isLF(ch)) {
					break;
				} else {
					throw new HttpException(HttpStatusCode.STATUS_400_BAD_REQUEST, "Your HTTP client's request contained an unallowed CR control character.");
				}
			}
      
			IHttpHeader header = parseHeader(is);
      httpHeaders.addHeader(header);
		}
    
    return httpHeaders;
	}
  
  @Override
  public IHttpHeader parseHeader(InputStream is) {
    HttpBuffer keyBuffer = readHeaderKey(is);
    String key = keyBuffer.toString();
    HttpBuffer valueBuffer = readHeaderValue(is);
  	String rawValue = valueBuffer.toString();
    
    return httpHeaderFactory.buildHttpHeader(key, rawValue);
  }
  
  @Override
  public IHttpRequestLine parseRequestLine(InputStream is) {
		HttpBuffer requestLineBuffer = readRequestLine(is);
		String requestLine = requestLineBuffer.toString();

		String[] result = requestLine.split("\\s");

    HttpMethod method;
    IHttpVersion version;
    URI uri;
    
		if (result.length == 3) {
			String methodString = result[0].trim();
			String uriString = result[1].trim();
			String versionString = result[2].trim();

      switch (methodString) {
        case "OPTIONS":
          method = HttpMethod.OPTIONS;
          break;
        case "GET":
          method = HttpMethod.GET;
          break;
        case "HEAD":
          method = HttpMethod.HEAD;
            break;
        case "POST":
          method = HttpMethod.POST;
            break;
        case "PUT":
          method = HttpMethod.PUT;
          throw new HttpException(HttpStatusCode.STATUS_405_METHOD_NOT_ALLOWED, "Method '" + methodString + "' not allowed!");
        case "DELETE":
          method = HttpMethod.DELETE;
          throw new HttpException(HttpStatusCode.STATUS_405_METHOD_NOT_ALLOWED, "Method '" + methodString + "' not allowed!");
        case "TRACE":
          method = HttpMethod.TRACE;
          throw new HttpException(HttpStatusCode.STATUS_405_METHOD_NOT_ALLOWED, "Method '" + methodString + "' not allowed!");
        case "CONNECT":
          method = HttpMethod.CONNECT;
          throw new HttpException(HttpStatusCode.STATUS_405_METHOD_NOT_ALLOWED, "Method '" + methodString + "' not allowed!");
        default:
          throw new HttpException(HttpStatusCode.STATUS_501_NOT_IMPLEMENTED, "Method '" + methodString + "' not implemented!");
      }

			try {
				uri = new URI(uriString);
			} catch (URISyntaxException e) {
				throw new HttpException(HttpStatusCode.STATUS_400_BAD_REQUEST, "URI '" + uriString + "' not valid!");
			}

			if (uri.getQuery() != null) {
				parseQuery(uri.getQuery());
			}

			if (versionString.compareTo(HttpUtils.httpVersion(1, 0)) == 0) {
				version = new HttpVersion(1, 0);
			} else if (versionString.compareTo(HttpUtils.httpVersion(1, 1)) == 0) {
        version = new HttpVersion(1, 1);
			} else {
				throw new HttpException(HttpStatusCode.STATUS_505_HTTP_VERSION_NOT_SUPPORTED, "Version '" + versionString + "' not supported!");
			}
		} else {
			throw new HttpException(HttpStatusCode.STATUS_400_BAD_REQUEST, "Request line '" + requestLine + "' invalid!");
		}
    
    return new HttpRequestLine(method, version, uri);
	}
  
 	private HttpBuffer readRequestLine(InputStream is) {
		HttpBuffer buffer = new HttpBuffer();

		int last = -1;
		int ch = -1;
    try {
      ch = is.read();
    } catch (IOException ex) {
      Logger.getLogger(HttpParser.class.getName()).log(Level.SEVERE, null, ex);
      throw new exceptions.IOException(ex);
    }

		while (isCR(ch) || isLF(ch)) {
			last = ch;
      try {
        ch = is.read();
      } catch (IOException ex) {
        Logger.getLogger(HttpParser.class.getName()).log(Level.SEVERE, null, ex);
        throw new exceptions.IOException(ex);
      }
		}

		while (true) {
			if (ch == -1) {
				// unepected end of input
				throw new HttpException(HttpStatusCode.STATUS_400_BAD_REQUEST, "Your HTTP client's request ended unexpectedly.");
			} else if (ch == '\r') {
				last = ch;
			} else if (ch == '\n') {
				if (last == '\r') {
					break;
				} else {
					throw new HttpException(HttpStatusCode.STATUS_400_BAD_REQUEST,
							"Your HTTP client's request contained an unallowed LF control character.");
				}
			} else {
				if (last == '\r') {
					throw new HttpException(HttpStatusCode.STATUS_400_BAD_REQUEST,
							"Your HTTP client's request contained an unallowed LF control character.");
				} else {
					last = ch;
					buffer.append(ch);
				}
			}
      try {
        ch = is.read();
      } catch (IOException ex) {
        Logger.getLogger(HttpParser.class.getName()).log(Level.SEVERE, null, ex);
        throw new exceptions.IOException(ex);
      }
		}

		return buffer;
	}
  
	protected HttpBuffer readHeaderKey(InputStream is) {
		HttpBuffer buffer = new HttpBuffer();

    int ch = -1;
    
		while (ch != ':') {
			if (ch == -1) {
				// unexpected end of input
				throw new HttpException(HttpStatusCode.STATUS_400_BAD_REQUEST, "Your HTTP client's request ended unexpectedly.");
			} else if (isCTL(ch)) {
				// CTL character not allowed
				throw new HttpException(HttpStatusCode.STATUS_400_BAD_REQUEST,
            String.format("Your HTTP client's request header contained an unallowed CTL character: '%1s'.", (char) (ch & 0xff)));
			} else if (isSeparator(ch)) {
				// separator character not allowed
				throw new HttpException(HttpStatusCode.STATUS_400_BAD_REQUEST,
						String.format("Your HTTP client's request header contained an unallowed separator character: '%1s'.", (char) (ch & 0xff)));
			} else {
				buffer.append(ch);
			}
      
      try {
        ch = is.read();
      } catch (IOException ex) {
        Logger.getLogger(HttpParser.class.getName()).log(Level.SEVERE, null, ex);
        throw new exceptions.IOException(ex);
      }
		}

		return buffer;
	}

	protected HttpBuffer readHeaderValue(InputStream is) {
		HttpBuffer buffer = new HttpBuffer();

		int last = -1;
    int ch = -1;
    
    try {
      ch = is.read();
    } catch (IOException ex) {
      Logger.getLogger(HttpParser.class.getName()).log(Level.SEVERE, null, ex);
      throw new exceptions.IOException(ex);
    }

		boolean insideQuote = false;

		// skip LWS
		while (isSP(ch) || isHT(ch)) {
			last = ch;
      try {
        ch = is.read();
      } catch (IOException ex) {
        Logger.getLogger(HttpParser.class.getName()).log(Level.SEVERE, null, ex);
        throw new exceptions.IOException(ex);
      }
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
            try {
              ch = is.read();
            } catch (IOException ex) {
              Logger.getLogger(HttpParser.class.getName()).log(Level.SEVERE, null, ex);
              throw new exceptions.IOException(ex);
            }

						if (isSP(ch) || isHT(ch)) {
							// we are in a continuation line, skip LWS
							while (isSP(ch) || isHT(ch)) {
								last = ch;
                try {
                  ch = is.read();
                } catch (IOException ex) {
                  Logger.getLogger(HttpParser.class.getName()).log(Level.SEVERE, null, ex);
                  throw new exceptions.IOException(ex);
                }
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
						throw new HttpException(HttpStatusCode.STATUS_400_BAD_REQUEST,
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
      try {
        ch = is.read();
      } catch (IOException ex) {
        Logger.getLogger(HttpParser.class.getName()).log(Level.SEVERE, null, ex);
        throw new exceptions.IOException(ex);
      }
		}

		return buffer;
	}
  
  protected Map<String, String> parseQuery(String query) {
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
						throw new HttpException(HttpStatusCode.STATUS_400_BAD_REQUEST, 
              String.format("Query part '%1s' is not valid!", pairs[i]));
					}
				}
			} catch (UnsupportedEncodingException e) {
				throw new HttpException(HttpStatusCode.STATUS_500_INTERNAL_SERVER_ERROR, 
          String.format("An error occured decoding query '%1s'!", query));
			}
		} else {
			throw new HttpException(HttpStatusCode.STATUS_500_INTERNAL_SERVER_ERROR, 
        String.format("An error occured decoding query '%1s'!", query));
		}

		return result;
	}

  @Override
  public void parseBody(InputStream is, IHttpHeaders headers) {
		LongHttpHeader contentLengthHeader = ((LongHttpHeader)headers.getHeader("Content-Length"));
		StringHttpHeader contentTypeHeader = ((StringHttpHeader)headers.getHeader("Content-Type"));
    StringHttpHeader transferEncodingHeader = ((StringHttpHeader)headers.getHeader("Transfer-Encoding"));
    
    String contentType = contentTypeHeader.getValue();
		String transferEncoding = transferEncodingHeader.getValue();

		if (transferEncoding == null) {
			if (contentLengthHeader == null) {
				// well, no body present, as it seems
				return;
			} else {
				if (contentType.compareTo("application/x-www-form-urlencoded") == 0) {
					try {
            long contentLength = contentLengthHeader.getValue();						
            readPlain(is, contentLength);
					} catch (NumberFormatException e) {
						throw new HttpException(HttpStatusCode.STATUS_400_BAD_REQUEST, 
              String.format("Header field Content-Length contained invalid value '%1s'.", contentLengthHeader.rawValue));
					}
				} else {
					throw new HttpException(HttpStatusCode.STATUS_415_UNSUPPORTED_MEDIA_TYPE, "Media type is not supported.");
				}
			}
		} else {
			if (transferEncoding.compareTo("chunked") == 0) {
				readChunked(is, headers);
			} else {
				throw new HttpException(HttpStatusCode.STATUS_501_NOT_IMPLEMENTED, 
          String.format("The Transfer-Encoding '%1s' is not supported.", transferEncoding));
			}
		}
	}
  
  protected void readPlain(InputStream is, long contentLength) {
		HttpBuffer buffer = new HttpBuffer();
		int written = 1;

    byte[] body;
    int ch = -1;
    try {
      ch = is.read();
    } catch (IOException ex) {
      Logger.getLogger(HttpParser.class.getName()).log(Level.SEVERE, null, ex);
      throw new exceptions.IOException(ex);
    }

		while (written < contentLength) {
			if (ch == -1) {
				throw new HttpException(HttpStatusCode.STATUS_400_BAD_REQUEST, "Unexpected end of stream.");
			} else {
				buffer.append(ch);
				written++;
			}
      try {
        ch = is.read();
      } catch (IOException ex) {
        Logger.getLogger(HttpParser.class.getName()).log(Level.SEVERE, null, ex);
        throw new exceptions.IOException(ex);
      }
		}

		body = buffer.getCopy();
	}

	protected void readChunked(InputStream is, IHttpHeaders headers) {
		HttpBuffer buffer = new HttpBuffer();
		ChunkedInputStream cis = new ChunkedInputStream(is, headers);

    byte[] body;
		int ch;
    try {
      ch = cis.read();
    } catch (IOException ex) {
      Logger.getLogger(HttpParser.class.getName()).log(Level.SEVERE, null, ex);
      throw new exceptions.IOException(ex);
    }

		while (true) {
			if (ch == -1) {
				break;
			} else {
				buffer.append(ch);
			}
		}

		body = buffer.getCopy();
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
