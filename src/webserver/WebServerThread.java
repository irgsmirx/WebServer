package webserver;

import http.*;
import java.io.*;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.URI;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class WebServerThread extends WebServer implements Runnable, HttpConstants, HttpMethods {

	final static int BUF_SIZE = 4096;

	static final byte SP = (byte) ' ';
	static final byte CR = (byte) '\r';
	static final byte LF = (byte) '\n';
	static final byte[] EOL = {CR, LF};

	/*
	 * buffer to use for requests
	 */
	protected byte[] buf;

	/*
	 * Socket to client we're handling
	 */
	private Socket socket;

	public WebServerThread() {
		buf = new byte[BUF_SIZE];
		socket = null;
	}

	public WebServerThread(Socket socket) {
		buf = new byte[BUF_SIZE];
		this.socket = socket;
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

			/*
			 * go back in wait queue if there'socket fewer than numHandler
			 * connections.
			 */
			socket = null;
			List<WebServerThread> pool = WebServer.threads;
			synchronized (pool) {
				if (pool.size() >= WebServer.threadlimit) {
					/* too many threads, exit this one */
					return;
				} else {
					pool.add(this);
				}
			}
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
			HttpContext httpContext = establishContext(is, os);
      
      handleContext(httpContext);
		}	catch (HttpException e) {
			HttpResponse hr = new HttpResponse(e);
			//hr.print(ps);
			//ps.flush();
		} catch (SocketTimeoutException e) {
			HttpResponse hr = new HttpResponse(e);
			//hr.print(ps);
			//ps.flush();
		}

		finally {
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
  
  private HttpContext establishContext(InputStream is, OutputStream os) throws IOException, HttpException {
		HttpRequest httpRequest = new HttpRequest();
		httpRequest.parse(is);

    HttpResponse httpResponse = new HttpResponse(new File(httpRequest.getUri().getPath()));
    
    HttpContext httpContext = new HttpContext(httpRequest, httpResponse);
    
    return httpContext;
  }
  
  private void handleContext(HttpContext context) {
    
  }

	private void handleRequest(HttpRequest request) {
    int method = request.getMethod();
		int major = request.getMajor();
		int minor = request.getMinor();
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
			returnCode = HTTP_NOT_FOUND;
			p.print("HTTP/1.0 " + HTTP_NOT_FOUND + " not found");
			p.write(EOL);
			result = false;
		} else if (!w.isConsistent()) {
			returnCode = HTTP_SERVICE_UNAVAILABLE;
			p.print("HTTP/1.0 " + HTTP_SERVICE_UNAVAILABLE + " not available");
			p.write(EOL);
			result = false;
		} else {
			returnCode = HTTP_OK;
			p.print("HTTP/1.0 " + HTTP_OK + " OK");
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
		int rCode = 0;
		if (!targ.exists()) {
			rCode = HTTP_NOT_FOUND;
			ps.print("HTTP/1.0 " + HTTP_NOT_FOUND + " not found");
			ps.write(EOL);
			ret = false;
		} else {
			rCode = HTTP_OK;
			ps.print("HTTP/1.0 " + HTTP_OK + " OK");
			ps.write(EOL);
			ret = true;
		}
		log("From " + socket.getInetAddress().getHostAddress() + ": GET " + targ.getAbsolutePath() + "-->" + rCode);
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

}
