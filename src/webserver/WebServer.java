package webserver;

/**
 * An example of a very simple, multi-threaded HTTP server.
 * Implementation notes are in WebServer.html, and also
 * as comments in the source code.
 */
import http.HttpApplicationFactory;
import http.HttpConstants;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

public class WebServer implements HttpConstants {

	/*
	 * the server's name and version
	 */
	public static String serverid = "Jackey/0.0.1";

	/*
	 * stream to log to
	 */
	protected static PrintStream log = null;

	/*
	 * our server's configuration information is stored in these properties
	 */
	protected static Properties props = new Properties();

	/*
	 * Where worker threads stand idle
	 */
	protected static final List<WebServerThread> threads = Collections.synchronizedList(new ArrayList<WebServerThread>());

	/*
	 * the web server's port
	 */
	protected static int port = 0;

	/*
	 * the web server's virtual root
	 */
	protected static File root = null;

	/*
	 * timeout on client connections
	 */
	protected static int timeout = 0;

	/*
	 * max # worker threads
	 */
	protected static int threadlimit = 0;

  
  protected static HttpApplicationFactory httpApplicationFactory;
  
	/*
	 * print to stdout
	 */
	protected static void p(String s) {
		System.out.println(s);
	}

	/*
	 * print to the log file
	 */
	protected static void log(String s) {
		synchronized (log) {
			log.println(s);
			log.flush();
		}
	}

	/*
	 * load server properties from user's home
	 */
	protected static void loadProps() throws IOException {
		String propFile = System.getProperty("user.home") + File.separator + ".jackey" + File.separator + "jackey.conf";

		File f = new File(propFile);

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

	protected static void printProps() {
		p("port=" + port);
		p("root=" + root);
		p("timeout=" + timeout);
		p("threadlimit=" + threadlimit);
	}

	public static void main(String[] args) throws Exception {
		ServerSocket serverSocket = null;
		boolean listening = true;

		loadProps();
		printProps();

		/*
		 * start worker threads
		 */
		for (int i = 0; i < threadlimit; ++i) {
			WebServerThread w = new WebServerThread();
			Thread t = new Thread(w, "thread #" + i);
			t.start();
			threads.add(w);

			// (new Thread(w, "worker #" + i)).start();
			// threads.addElement(w);
		}

		try {
			serverSocket = new ServerSocket(port);
		} catch (IOException e) {
			log("Could not listen on port " + port + "!");
			System.exit(1);
		}

		while (listening) {
			Socket socket = serverSocket.accept();

			WebServerThread w;
			synchronized (threads) {
				if (threads.isEmpty()) {
					WebServerThread ws = new WebServerThread(socket);
					ws.setSocket(socket);
					Thread t = new Thread(ws, "webserverthread");
					t.start();
				} else {
					w = threads.get(0);
					threads.remove(0);
					w.setSocket(socket);
				}
			}
		}

		serverSocket.close();
	}
}
