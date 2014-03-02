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
        int port = 0;
        File root = new File("");

        if (f.exists()) {
            try (InputStream is = new BufferedInputStream(new FileInputStream(f))) {
                configurationProperties.load(is);
            }

            String r = configurationProperties.getProperty("port");
            if (r != null) {
                port = Integer.parseInt(r);
            }

            r = configurationProperties.getProperty("root");
            if (r != null) {
                root = new File(r);
                if (!root.exists()) {
                    throw new Error(root + " doesn't exist as server root");
                }
            }

            r = configurationProperties.getProperty("timeout");
            if (r != null) {
                clientConnectionTimeout = Integer.parseInt(r);
            }

            r = configurationProperties.getProperty("threadlimit");
            if (r != null) {
                maximumNumberOfWorkerThreads = Integer.parseInt(r);
            }
        }

        /* if no properties were specified, choose defaults */
        if (port == 0) {
            port = 8080;
        }

        if (root == null) {
            root = new File(System.getProperty("user.dir"));
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
