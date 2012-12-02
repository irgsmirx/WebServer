/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ramforth.webserver.http;

import com.ramforth.webserver.WebServerThread;
import com.ramforth.webserver.exceptions.AlreadyListeningException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ServerSocketFactory;
import javax.net.ssl.SSLServerSocketFactory;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 * @author Tobias Ramforth <tobias.ramforth at tu-dortmund.de>
 */
public class HttpsListener implements IHttpListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(HttpsListener.class);

    private int port = 0;
    private int backlog = 0;
    private InetAddress listenAddress;
    private boolean listening = false;
    private static final ServerSocketFactory serverSocketFactory = SSLServerSocketFactory.getDefault();
    private ServerSocket listeningSocket;
    private int acceptedSockets = 0;
    private ExecutorService threadPool;
    private IHttpContextHandler contextHandler = null;

    public HttpsListener(int port) {
        try {
            this.listenAddress = InetAddress.getLocalHost();
        }
        catch (UnknownHostException ex) {
            LOGGER.warn("Error", ex); //TODO Enter precise error message
            throw new com.ramforth.webserver.exceptions.UnknownHostException(ex);
        }
        this.port = port;

        initializeThreadPool();
    }

    public HttpsListener(InetAddress listenAddress, int port) {
        this.listenAddress = listenAddress;
        this.port = port;
    }

    private void initializeThreadPool() {
        threadPool = Executors.newFixedThreadPool(10);
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
        listening = true;

        while (listening) {
            try {
                Socket socket = listeningSocket.accept();
                WebServerThread wst = new WebServerThread(socket);

                wst.setContextHandler(contextHandler);

                threadPool.execute(wst);
            }
            catch (IOException ex) {
                if (listening) {
                    throw new com.ramforth.webserver.exceptions.AlreadyListeningException(ex);
                } else {
                    LOGGER.warn("Error", ex); //TODO Enter precise error message
                    return;
                }
            }
        }

        threadPool.shutdown();

        stopListening();
    }

    private void createServerSocket() {
        try {
            listeningSocket = serverSocketFactory.createServerSocket(port, backlog, listenAddress);
        }
        catch (IOException ex) {
            LOGGER.warn("Error", ex); //TODO Enter precise error message
            throw new com.ramforth.webserver.exceptions.IOException(ex);
        }
    }

    @Override
    public void stopListening() {
        listening = false;

        try {
            listeningSocket.close();
        }
        catch (IOException ex) {
            LOGGER.warn("Error", ex); //TODO Enter precise error message
            throw new com.ramforth.webserver.exceptions.IOException(ex);
        }

        listeningSocket = null;
    }

    @Override
    public boolean isListening() {
        return listening;
    }

    @Override
    public void setContextHandler(IHttpContextHandler value) {
        this.contextHandler = value;
    }

    @Override
    public void unsetContextHandler() {
        this.contextHandler = null;
    }
}
