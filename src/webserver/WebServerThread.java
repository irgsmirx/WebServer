package webserver;

import http.headers.StringHttpHeader;
import http.parsers.HttpRequestParser;
import http.parsers.IHttpRequestParser;
import exceptions.HttpException;
import exceptions.ResourceNotFoundException;
import http.*;
import http.handlers.HttpRequestHandlers;
import http.handlers.IHttpRequestHandler;
import http.handlers.IHttpRequestHandlers;
import java.io.*;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;
import web.ConnectionType;

public class WebServerThread implements Runnable {

	final static int BUF_SIZE = 4096;

	static final byte SP = (byte) ' ';
	static final byte CR = (byte) '\r';
	static final byte LF = (byte) '\n';
	static final byte[] EOL = {CR, LF};

	/*
	 * buffer to use for requests
	 */
	protected byte[] buf;

	private Socket socket;
  private IHttpContext context;
  
  private IHttpRequestHandlers requestHandlers = new HttpRequestHandlers();
  //private IHttpModules modules = new HttpModules();
  private IHttpContextHandler contextHandler = null;
  
	public WebServerThread(Socket socket) {
    initializeBuffer();
		this.socket = socket;
	}

	public WebServerThread() {
    this(null);
	}


  private void initializeBuffer() {
		buf = new byte[BUF_SIZE];
  }
  
	synchronized void setSocket(Socket s) {
		this.socket = s;
		notify();
	}

  @Override
	public synchronized void run() {
		//while (true) {
			if (socket == null) {
				/* nothing to do */
				try {
					wait();
				} catch (InterruptedException e) {
					/* should not happen */
					//continue;
				}
			}
			try {
				handleClient();
			} catch (IOException ex) {
				Logger.getLogger(WebServerThread.class.getName()).log(Level.SEVERE, null, ex);
			}

			socket = null;
		//}
	}

	private void handleClient() throws IOException, SocketTimeoutException {
		InputStream is = new BufferedInputStream(socket.getInputStream());
		OutputStream os = new BufferedOutputStream(socket.getOutputStream());

		/*
		 * we will only block in read for this many milliseconds before we fail with
		 * java.io.InterruptedIOException, at which point we will abandon the
		 * connection.
		 */
		socket.setSoTimeout(1000000/* WebServer.timeout */);
		socket.setTcpNoDelay(true);

    clearBuffer();

		try {
			context = establishContext(is, os);
      
      handleContext(context);
    } catch (ResourceNotFoundException rnfex) {
			IHttpResponse httpResponse = createHttpResponseForStatusCode(HttpStatusCode.STATUS_404_NOT_FOUND);
      
      IHttpResponseWriter responseWriter = new HttpResponseWriter(os);
      responseWriter.writeResponse(httpResponse);
		}	catch (HttpException he) {
			IHttpResponse httpResponse = createHttpResponseForStatusCode(he.getStatusCode());
      
      IHttpResponseWriter responseWriter = new HttpResponseWriter(os);
      responseWriter.writeResponse(httpResponse);
		} catch (SocketTimeoutException ste) {
			IHttpResponse httpResponse = createHttpResponseForStatusCode(HttpStatusCode.STATUS_408_REQUEST_TIMEOUT);

      IHttpResponseWriter responseWriter = new HttpResponseWriter(os);
      responseWriter.writeResponse(httpResponse);
    } finally {
      if (!socket.isClosed()) {
        try {
          os.flush();

          is.close();
          os.close();

          socket.close();
        } catch (Exception ex) {
          
        }
      }
		}
	}
  
  private void clearBuffer() {
    /* zero out the buffer from last time */
		for (int i = 0; i < BUF_SIZE; i++) {
			buf[i] = 0;
		}
  }
  
  private IHttpContext establishContext(InputStream is, OutputStream os) throws IOException, HttpException {
    try {
      IHttpRequest httpRequest = parseHttpRequest(is);
      httpRequest.setInputStream(is);

      IHttpResponse httpResponse = createHttpResponseFromHttpRequest(httpRequest);
      httpResponse.setOutputStream(os);

      return new HttpContext(httpRequest, httpResponse);
    } catch (Exception ex) {
			throw new HttpException(HttpStatusCode.STATUS_400_BAD_REQUEST, "Your HTTP client's request ended unexpectedly.");
    }
  }
  
  private IHttpRequest parseHttpRequest(InputStream is) {
    IHttpRequestParser httpParser = new HttpRequestParser();
    
    return httpParser.parseRequest(is);
  }
  
  private IHttpResponse createHttpResponseForStatusCode(IHttpStatusCode statusCode) {
    IHttpResponse httpResponse = new HttpResponse(HttpVersion.HTTP_11, statusCode);
    
    addDefaultHeadersToResponse(httpResponse);
    
    return httpResponse;
  }
  
  private IHttpResponse createHttpResponseFromHttpRequest(IHttpRequest httpRequest) {
    IHttpResponse httpResponse;
    
    if (httpRequest.getVersion().isHTTP11()) {
      httpResponse = new HttpResponse(HttpVersion.HTTP_11);
      httpResponse.setConnectionType(ConnectionType.KEEP_ALIVE);
      httpResponse.getHeaders().addHeader(new StringHttpHeader("Keep-Alive", "timeout=5, max=100"));
    } else {
      httpResponse = new HttpResponse(HttpVersion.HTTP_10);
      httpResponse.setConnectionType(ConnectionType.CLOSE);
    }

    addDefaultHeadersToResponse(httpResponse);
    
    return httpResponse;
  }

  private void addDefaultHeadersToResponse(IHttpResponse httpResponse) {
		SimpleDateFormat formatter = new SimpleDateFormat("EEE, dd MMM yyyy hh:mm:ss zzz", Locale.US);
		formatter.setTimeZone(TimeZone.getTimeZone("GMT"));
		Date date = new Date();

    httpResponse.setVersion(HttpVersion.HTTP_11);
    httpResponse.getHeaders().addHeader(new StringHttpHeader("Date", formatter.format(date)));
    httpResponse.getHeaders().addHeader(new StringHttpHeader("Connection", "close"));
  }
  
  private void handleRequest(IHttpRequest request) {
    for (IHttpRequestHandler handler : requestHandlers) {
      handler.handleRequest(request);
    }
  }
  
  private void handleContext(IHttpContext context) {
    if (contextHandler != null) {
      contextHandler.handleContext(context);
    }
  }

	private boolean printHeaders(WebContainer w, PrintStream p) throws IOException {
		boolean result = false;
		int returnCode = 0;

		if (w == null) {
			returnCode = HttpStatusCode.STATUS_404_NOT_FOUND.getCode();
			p.print("HTTP/1.0 " + returnCode + " not found");
			p.write(EOL);
			result = false;
		} else if (!w.isConsistent()) {
			returnCode = HttpStatusCode.STATUS_503_SERVICE_UNAVAILABLE.getCode();
			p.print("HTTP/1.0 " + returnCode + " not available");
			p.write(EOL);
			result = false;
		} else {
			returnCode = HttpStatusCode.STATUS_200_OK.getCode();
			p.print("HTTP/1.0 " + returnCode + " OK");
			p.write(EOL);
			result = true;
		}

		// log("From " + socket.getInetAddress().getHostAddress() + ": GET " +
		// w.getFilename() + "-->" + returnCode);

		p.print("Server: Jackey 0.0.1");
		p.write(EOL);
		p.print("Date: " + (new Date()));
		p.write(EOL);

		if (result) {
			p.print("Content-length: " + w.getContentLength());
			p.write(EOL);
			p.print("Last Modified: " + (new Date()));
			p.write(EOL);
		}

		return result;
	}

	private boolean printHeaders(File targ, PrintStream ps) throws IOException {
		boolean ret = false;
		int returnCode = 0;
		if (!targ.exists()) {
			returnCode = HttpStatusCode.STATUS_404_NOT_FOUND.getCode();
			ps.print("HTTP/1.0 " + returnCode + " not found");
			ps.write(EOL);
			ret = false;
		} else {
			returnCode = HttpStatusCode.STATUS_200_OK.getCode();
			ps.print("HTTP/1.0 " + returnCode + " OK");
			ps.write(EOL);
			ret = true;
		}
		//log("From " + socket.getInetAddress().getHostAddress() + ": GET " + targ.getAbsolutePath() + "-->" + rCode);
		ps.print("Server: Simple java");
		ps.write(EOL);
		ps.print("Date: " + (new Date()));
		ps.write(EOL);
		if (ret) {
			if (!targ.isDirectory()) {
				ps.print("Content-length: " + targ.length());
				ps.write(EOL);
				ps.print("Last Modified: " + (new Date(targ.lastModified())));
				ps.write(EOL);
				String name = targ.getName();
				int ind = name.lastIndexOf('.');
				String ct = null;
				if (ind > 0) {
					//ct = (String) map.get(name.substring(ind));
				}
				if (ct == null) {
					ct = "unknown/unknown";
				}
				ps.print("Content-type: " + ct);
				ps.write(EOL);
			} else {
				ps.print("Content-type: text/html");
				ps.write(EOL);
			}
		}
		return ret;
	}

	// private void sendFile(File targ, PrintStream ps) throws IOException {
	// InputStream is = null;
	// ps.write(EOL);
	// if (targ.isDirectory()) {
	// listDirectory(targ, ps);
	// return;
	// } else {
	// is = new FileInputStream(targ.getAbsolutePath());
	// }
	//
	// try {
	// int n;
	// while ((n = is.read(buf)) > 0) {
	// ps.write(buf, 0, n);
	// }
	// } finally {
	// is.close();
	// }
	// }

	private void sendWebContainer(WebContainer w, PrintStream p) throws IOException {
		if (w != null) {
			p.write(EOL);
			w.print(p);
		}
	}
  
  public void addRequestHandler(IHttpRequestHandler requestHandler) {
    requestHandlers.add(requestHandler);
  }
 
  public void setContextHandler(IHttpContextHandler contextHandler) {
    this.contextHandler = contextHandler;
  }
  
}
