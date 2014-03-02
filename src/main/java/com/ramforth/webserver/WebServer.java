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
package com.ramforth.webserver;

import com.ramforth.utilities.common.implementation.SystemProperties;
import com.ramforth.webserver.http.HttpApplicationFactory;
import com.ramforth.webserver.http.IHttpContext;
import com.ramforth.webserver.http.IHttpContextHandler;
import com.ramforth.webserver.http.IHttpListener;
import com.ramforth.webserver.http.IHttpRequest;
import com.ramforth.webserver.http.handlers.IHttpRequestHandler;
import com.ramforth.webserver.http.modules.IHttpModule;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WebServer implements IHttpRequestHandler, IHttpContextHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(WebServer.class);
    
    private boolean running;

    public String serverid = "com.ramforth.webserver/0.0.2";

    protected Properties configurationProperties = new Properties();

    protected final List<IHttpListener> httpListeners = Collections.synchronizedList(new ArrayList<IHttpListener>());
    protected final List<IHttpRequestHandler> requestHandlers = Collections.synchronizedList(new ArrayList<IHttpRequestHandler>());
    protected final List<IHttpModule> modules = Collections.synchronizedList(new ArrayList<IHttpModule>());

    protected int clientConnectionTimeout = 0;
    protected int maximumNumberOfWorkerThreads = 0;

    protected static HttpApplicationFactory httpApplicationFactory;

    protected void loadConfiguration() throws IOException {
        String propertiesFile = SystemProperties.getUserHomeDirectory() + File.separator + ".com.ramforth.webserver" + File.separator + "webserver.conf";

        File f = new File(propertiesFile);

        if (f.exists()) {
            try (InputStream is = new BufferedInputStream(new FileInputStream(f))) {
                configurationProperties.load(is);
            }

            String clientConnectionTimeoutStringValue = configurationProperties.getProperty("clientConnectionTimeout");
            if (clientConnectionTimeoutStringValue != null) {
                try {
                    clientConnectionTimeout = Integer.parseInt(clientConnectionTimeoutStringValue);
                } catch (NumberFormatException nfex) {
                    LOGGER.warn(String.format("Could not parse clientConnectionTimeout (%s).", clientConnectionTimeoutStringValue), nfex);
                }
            }

            String maximumNumberOfWorkerThreadsStringValue = configurationProperties.getProperty("maximumNumberOfWorkerThreads");
            if (maximumNumberOfWorkerThreadsStringValue != null) {
                try {
                    maximumNumberOfWorkerThreads = Integer.parseInt(maximumNumberOfWorkerThreadsStringValue);
                } catch (NumberFormatException nfex) {
                    LOGGER.warn(String.format("Could not parse maximumNumberOfWorkerThreads (%s).", maximumNumberOfWorkerThreadsStringValue), nfex);
                }
            }
        }

        if (clientConnectionTimeout <= 1000) {
            clientConnectionTimeout = 5000;
        }

        if (maximumNumberOfWorkerThreads == 0) {
            maximumNumberOfWorkerThreads = 5;
        }
    }

    protected void logProperties() {
        LOGGER.debug("timeout=" + clientConnectionTimeout);
        LOGGER.debug("threadlimit=" + maximumNumberOfWorkerThreads);
    }

    public boolean isRunning() {
        return running;
    }

    public void start() {
        for (IHttpListener listener : httpListeners) {
            //listener.ErrorPageRequested += Listener_OnErrorPage;
            //listener.RequestReceived += OnRequest;
            //listener.ContentLengthLimit = ContentLengthLimit;
            listener.setContextHandler(this);
            new Thread((Runnable) listener).start();
        }
        running = true;
    }

    public void stop() {
        for (IHttpListener listener : httpListeners) {
            //listener.ErrorPageRequested += Listener_OnErrorPage;
            //listener.RequestReceived += OnRequest;
            //listener.ContentLengthLimit = ContentLengthLimit;
            listener.unsetContextHandler();
            listener.stopListening();
        }
        running = false;
    }

    public void addRequestHandler(IHttpRequestHandler requestHandler) {
        requestHandlers.add(requestHandler);
    }

    @Override
    public void handleRequest(IHttpRequest request) {
        for (IHttpRequestHandler requestHandler : requestHandlers) {
            requestHandler.handleRequest(request);
        }
    }

    public void addModule(IHttpModule module) {
        modules.add(module);
    }

    @Override
    public synchronized void handleContext(IHttpContext context) {
        Iterator<IHttpModule> moduleIterator = modules.iterator();

        boolean processed = false;
        while (moduleIterator.hasNext()) {
            IHttpModule module = moduleIterator.next();
            try {
                if (module.processHttpContext(context)) {
                    break;
                }
            }
            catch (Exception ex) {
                if (!moduleIterator.hasNext()) {
                    throw ex;
                }
            }
        }
    }

    public void addHttpListener(IHttpListener listener) {
        this.httpListeners.add(listener);
    }

    public final Iterable<IHttpListener> getHttpListeners() {
        return this.httpListeners;
    }

}
