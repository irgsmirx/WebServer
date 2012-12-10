/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ramforth.webserver.http;

import com.ramforth.webserver.WebServerThread;
import com.ramforth.webserver.exceptions.AlreadyListeningException;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.net.ServerSocketFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Tobias Ramforth <tobias.ramforth at tu-dortmund.de>
 */
public abstract class AbstractHttpListener implements IHttpListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractHttpListener.class);

    private int port = 0;
    private InetAddress listenAddress;
    private boolean listening = false;
    protected static ServerSocketFactory serverSocketFactory;
    private ServerSocket listeningSocket;
    private int acceptedSockets = 0;
    private ExecutorService threadPool;
    private IHttpContextHandler contextHandler = null;
    
    public AbstractHttpListener(int port) {
        try {
            this.listenAddress = InetAddress.getLocalHost();
        }
        catch (UnknownHostException ex) {
            LOGGER.warn("Localhost could not be resolved to an address!", ex);
            throw new com.ramforth.webserver.exceptions.UnknownHostException(ex);
        }
        this.port = port;

        initializeThreadPool();
    }

    public AbstractHttpListener(InetAddress listenAddress, int port) {
        this.listenAddress = listenAddress;
        this.port = port;

        initializeThreadPool();
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
                    LOGGER.warn("I/O error while waiting for a connection.", ex);
                    return;
                }
            }
        }

        threadPool.shutdown();

        stopListening();
    }

    private void createServerSocket() {
        try {
            int backlog = 0;
            listeningSocket = serverSocketFactory.createServerSocket(port, backlog, listenAddress);
        }
        catch (IOException ex) {
            LOGGER.warn("Network error while creating the socket.", ex);
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
            LOGGER.warn("I/O error while closing the socket.", ex);
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
