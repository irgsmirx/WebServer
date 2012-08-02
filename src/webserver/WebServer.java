package webserver;

/**
 * An example of a very simple, multi-threaded HTTP server.
 * Implementation notes are in WebServer.html, and also
 * as comments in the source code.
 */
import http.HttpApplicationFactory;
import http.HttpConstants;
import http.IHttpListener;
import http.IModule;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

public class WebServer implements HttpConstants {

  private boolean running;
  
	/*
	 * the server's name and version
	 */
	public String serverid = "Jackey/0.0.1";

	/*
	 * stream to log to
	 */
	protected PrintStream log = null;

	/*
	 * our server's configuration information is stored in these properties
	 */
	protected Properties props = new Properties();

  protected final List<IHttpListener> httpListeners = Collections.synchronizedList(new ArrayList<IHttpListener>());
  protected final List<IModule> modules = Collections.synchronizedList(new ArrayList<IModule>());
	
	/*
	 * timeout on client connections
	 */
	protected int timeout = 0;

	/*
	 * max # worker threads
	 */
	protected int threadlimit = 0;

  
  protected static HttpApplicationFactory httpApplicationFactory;
  
	/*
	 * print to stdout
	 */
	protected void p(String s) {
		System.out.println(s);
	}

	/*
	 * print to the log file
	 */
	protected void log(String s) {
		synchronized (log) {
			log.println(s);
			log.flush();
		}
	}

	/*
	 * load server properties from user's home
	 */
	protected void loadProps() throws IOException {
		String propFile = System.getProperty("user.home") + File.separator + ".jackey" + File.separator + "jackey.conf";

		File f = new File(propFile);
    int port = 0;
    File root = new File("");
            
		if (f.exists()) {
      try (InputStream is = new BufferedInputStream(new FileInputStream(f))) {
        props.load(is);
      }

			String r = props.getProperty("port");
			if (r != null) {
				port = Integer.parseInt(r);
			}

			r = props.getProperty("root");
			if (r != null) {
				root = new File(r);
				if (!root.exists()) {
					throw new Error(root + " doesn't exist as server root");
				}
			}

			r = props.getProperty("timeout");
			if (r != null) {
				timeout = Integer.parseInt(r);
			}

			r = props.getProperty("threadlimit");
			if (r != null) {
				threadlimit = Integer.parseInt(r);
			}

			r = props.getProperty("log");
			if (r != null) {
				p("opening log file: " + r);
				log = new PrintStream(new BufferedOutputStream(new FileOutputStream(r)));
			}
		}

		/* if no properties were specified, choose defaults */
		if (port == 0) {
			port = 8080;
		}

		if (root == null) {
			root = new File(System.getProperty("user.dir"));
		}

		if (timeout <= 1000) {
			timeout = 5000;
		}

		if (threadlimit == 0) {
			threadlimit = 5;
		}

		if (log == null) {
			p("logging to stdout");
			log = System.out;
		}
	}

	protected void printProps() {
		//p("port=" + port);
		//p("root=" + root);
		p("timeout=" + timeout);
		p("threadlimit=" + threadlimit);
	}

  public boolean isRunning() {
    return running;
  }
  
  public void start() {
    for (IHttpListener listener : httpListeners) {
      //listener.ErrorPageRequested += Listener_OnErrorPage;
      //listener.RequestReceived += OnRequest;
      //listener.ContentLengthLimit = ContentLengthLimit;
      listener.startListening();
    }
    running = true;
  }
  
  public void stop() {
    for (IHttpListener listener : httpListeners) {
      //listener.ErrorPageRequested += Listener_OnErrorPage;
      //listener.RequestReceived += OnRequest;
      //listener.ContentLengthLimit = ContentLengthLimit;
      listener.stopListening();
    }
    running = false;
  }
  
}
