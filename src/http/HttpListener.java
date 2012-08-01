/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package http;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import webserver.WebServerThread;

/**
 *
 * @author Tobias Ramforth <tobias.ramforth at tu-dortmund.de>
 */
public class HttpListener implements IHttpListener {

  private int port = 0;
  private int backlog = 0;
  private InetAddress listenAddress;
  private boolean listening = false;
  
  private ServerSocket listeningSocket;
  
  private int acceptedSockets = 0;
  
  private ExecutorService threadPool2;
  private List<WebServerThread> threadPool;
  
  public HttpListener(int port) {
    try {
      this.listenAddress = InetAddress.getLocalHost();
    } catch (UnknownHostException ex) {
      Logger.getLogger(HttpListener.class.getName()).log(Level.SEVERE, null, ex);
      throw new http.UnknownHostException(ex);
    }
    this.port = port;
  }
  
  public HttpListener(InetAddress listenAddress, int port) {
    this.listenAddress = listenAddress;
    this.port = port;
  }
  
  private void initializeThreadPool() {
    threadPool = new ArrayList<>();
    createThreads();
    
    threadPool2 = Executors.newFixedThreadPool(10);
  }
  
  private void createThreads() {
    for (int i = 0; i < 10; i++) {
      WebServerThread webServerThread = new WebServerThread();
      threadPool.add(webServerThread);
    }
  }
  
  @Override
  public int getPort() {
    return port;
  }

  @Override
  public void setPort(int value) {
    if (listening) {
      throw new AlreadyListeningException();
    } else {
      this.port = value;
    }
  }

  @Override
  public int getBacklog() {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public void setBacklog(int value) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public InetAddress getListenAddress() {
    return listenAddress;
  }

  @Override
  public void setListenAddress(InetAddress value) {
    if (listening) {
      throw new AlreadyListeningException();
    } else {
      this.listenAddress = value;
    }  
  }

  @Override
  public void startListening() {
		if (listeningSocket != null) {
      throw new AlreadyListeningException();
    }

    createServerSocket();
    listening  = true;
    
		try {
      while (listening) {
        Socket socket = listeningSocket.accept();
                
        WebServerThread wst;
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

      stopListening();
		} catch (IOException e) {
			//log("Could not listen on port " + port + "!");
			System.exit(1);
		} finally {
      listening = false;
      listeningSocket = null;
    }
  }

  private void createServerSocket() {
    try {
      listeningSocket = new ServerSocket(port, backlog, listenAddress);
    } catch (IOException ex) {
      Logger.getLogger(HttpListener.class.getName()).log(Level.SEVERE, null, ex);
      throw new http.IOException(ex);
    }
  }
  
  @Override
  public void stopListening() {
    try {
      listeningSocket.close();
    } catch (IOException ex) {
      Logger.getLogger(HttpListener.class.getName()).log(Level.SEVERE, null, ex);
      throw new http.IOException(ex);
    }
  }

  @Override
  public boolean isListening() {
    return listening;
  }
  
}
