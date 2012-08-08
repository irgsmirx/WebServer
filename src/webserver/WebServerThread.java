package webserver;

import exceptions.HttpException;
import http.*;
import java.io.*;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.URI;
import java.util.Date;
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
  
  private IHttpContextHandlers contextHandlers = new HttpContextHandlers();
  
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
		while (true) {
			if (socket == null) {
				/* nothing to do */
				try {
					wait();
				} catch (InterruptedException e) {
					/* should not happen */
					continue;
				}
			}
			try {
				handleClient();
			} catch (IOException ex) {
				Logger.getLogger(WebServerThread.class.getName()).log(Level.SEVERE, null, ex);
			}

			socket = null;
		}
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
		}	catch (HttpException e) {
			HttpResponse hr = new HttpResponse(e);
			//hr.print(ps);
			//ps.flush();
		} catch (SocketTimeoutException e) {
			HttpResponse hr = new HttpResponse(e);
			//hr.print(ps);
			//ps.flush();
		} finally {
      is.close();
			
      os.flush();
      os.close();
			
      socket.close();
		}
	}
  
  private void clearBuffer() {
    /* zero out the buffer from last time */
		for (int i = 0; i < BUF_SIZE; i++) {
			buf[i] = 0;
		}
  }
  
  private IHttpContext establishContext(InputStream is, OutputStream os) throws IOException, HttpException {
    IHttpContext httpContext = tryParseHttpRequest(is, os);
    
    return httpContext;
  }
  
  private IHttpContext tryParseHttpRequest(InputStream is, OutputStream os) {
    IHttpContext httpContext = null;
    
    try {
      IHttpParser httpParser = new HttpParser();

      IHttpRequest httpRequest;
      IHttpResponse httpResponse;
    
      httpRequest = httpParser.parseRequest(is);
    
      httpResponse = new HttpResponse(new File(httpRequest.getUri().getPath()));
      if (httpRequest.getVersion().isHTTP11()) {
        httpResponse.setConnectionType(ConnectionType.KEEP_ALIVE);
        httpResponse.getHeaders().addHeader(new StringHttpHeader("Keep-Alive", "timeout=5, max=100"));
      } else {
        httpResponse.setConnectionType(ConnectionType.DISCONNECT);
      }
      
      httpContext = new HttpContext(httpRequest, httpResponse);
    } catch (Exception ex) {
      try {
        IHttpResponse httpResponse = new HttpResponse()
        
        os.flush();
        os.close();
      } catch (Exception ex2) {
        
      }
    } finally {
      return httpContext;
    }
  }
  
  private void handleContext(IHttpContext context) {
    for (IHttpContextHandler handler : contextHandlers) {
      handler.handleContext(context);
    }
  }

	private void handleRequest(HttpRequest request) {
    HttpMethod method = request.getMethod();
		IHttpVersion version = request.getVersion();

    URI uri = request.getUri();
		byte[] body = request.getBody();

		switch (method) {
			case OPTIONS :
				break;
			case GET :
				break;
			case HEAD :
				break;
			case POST :
				break;
			case PUT :
				break;
			case DELETE :
				break;
			case TRACE :
				break;
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
					ct = (String) map.get(name.substring(ind));
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

	/* mapping of file extensions to content-types */
	static java.util.Hashtable map = new java.util.Hashtable();

	static {
		fillMap();
	}
	static void setSuffix(String k, String v) {
		map.put(k, v);
	}

	static void fillMap() {
		setSuffix("", "content/unknown");
		setSuffix(".uu", "application/octet-stream");
		setSuffix(".exe", "application/octet-stream");
		setSuffix(".ps", "application/postscript");
		setSuffix(".zip", "application/zip");
		setSuffix(".sh", "application/x-shar");
		setSuffix(".tar", "application/x-tar");
		setSuffix(".snd", "audio/basic");
		setSuffix(".au", "audio/basic");
		setSuffix(".wav", "audio/x-wav");
		setSuffix(".gif", "image/gif");
		setSuffix(".jpg", "image/jpeg");
		setSuffix(".jpeg", "image/jpeg");
		setSuffix(".htm", "text/html");
		setSuffix(".html", "text/html");
		setSuffix(".text", "text/plain");
		setSuffix(".c", "text/plain");
		setSuffix(".cc", "text/plain");
		setSuffix(".c++", "text/plain");
		setSuffix(".h", "text/plain");
		setSuffix(".pl", "text/plain");
		setSuffix(".txt", "text/plain");
		setSuffix(".java", "text/plain");
	}
  
  public void addContextHandler(IHttpContextHandler contextHandler) {
    contextHandlers.add(contextHandler);
  }

}
