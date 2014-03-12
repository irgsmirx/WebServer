/* 
 * Copyright (C) 2014 Tobias Ramforth
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
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
public abstract class AbstractHttpListener implements IHttpListener, Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractHttpListener.class);

    private int port = 0;
    private InetAddress listenAddress;
    private boolean listening = false;
    protected static ServerSocketFactory serverSocketFactory;
    private ServerSocket listeningSocket;
    private final int acceptedSockets = 0;
    private ExecutorService threadPool;
      
    private long maximumRequestLengthInBytes = 4096;
    
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
                wst.setMaximumRequestLengthInBytes(maximumRequestLengthInBytes);

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

    @Override
    public void run() {
        startListening();
    }

    @Override
    public void setMaximumRequestLengthInBytes(long value) {
        this.maximumRequestLengthInBytes = value;
    }

    @Override
    public final long getMaximumRequestLengthInBytes() {
        return this.maximumRequestLengthInBytes;
    }
    
    
    
}
