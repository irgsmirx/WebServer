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

import com.ramforth.webserver.exceptions.HttpException;
import com.ramforth.webserver.exceptions.ResourceNotFoundException;
import com.ramforth.webserver.http.*;
import com.ramforth.webserver.http.handlers.HttpRequestHandlers;
import com.ramforth.webserver.http.handlers.IHttpRequestHandler;
import com.ramforth.webserver.http.handlers.IHttpRequestHandlers;
import com.ramforth.webserver.http.headers.StringHttpHeader;
import com.ramforth.webserver.http.parsers.ContentLengthInputStream;
import com.ramforth.webserver.http.parsers.HttpRequestParser;
import com.ramforth.webserver.http.parsers.IHttpRequestParser;
import com.ramforth.webserver.web.ConnectionType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class WebServerThread implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(WebServerThread.class);

    static final int BUF_SIZE = 4096;
    static final byte SP = (byte) ' ';
    static final byte CR = (byte) '\r';
    static final byte LF = (byte) '\n';
    static final byte[] EOL = {CR, LF};

    /*
     * buffer to use for requests
     */
    protected byte[] buf;
    private Socket socket;
    private final IHttpRequestHandlers requestHandlers = new HttpRequestHandlers();

    private IHttpContextHandler contextHandler = null;

    private long maximumRequestLengthInBytes = 4096;
    
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
        //while (true) {
        if (socket == null) {
            /* nothing to do */
            try {
                wait();
            }
            catch (InterruptedException e) {
                /* should not happen */
                //continue;
            }
        }
        try {
            handleClient();
        }
        catch (IOException ex) {
            LOGGER.warn("Error handling incoming client request!", ex);
        }

        socket = null;
        //}
    }

    private void handleClient() throws IOException, SocketTimeoutException {
        InputStream is = new ContentLengthInputStream(new BufferedInputStream(socket.getInputStream()), maximumRequestLengthInBytes);
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
            IHttpContext context = establishContext(is, socket.getInetAddress(), os);

            handleContext(context);
        }
        catch (ResourceNotFoundException rnfex) {
            IHttpResponse httpResponse = createHttpResponseForStatusCode(HttpStatusCode.STATUS_404_NOT_FOUND);

            IHttpResponseWriter responseWriter = new HttpResponseWriter(os);
            responseWriter.writeResponse(httpResponse);
        }
        catch (HttpException he) {
            IHttpResponse httpResponse = createHttpResponseForStatusCode(he.getStatusCode());

            IHttpResponseWriter responseWriter = new HttpResponseWriter(os);
            responseWriter.writeResponse(httpResponse);
        }
        catch (SocketTimeoutException ste) {
            IHttpResponse httpResponse = createHttpResponseForStatusCode(HttpStatusCode.STATUS_408_REQUEST_TIMEOUT);

            IHttpResponseWriter responseWriter = new HttpResponseWriter(os);
            responseWriter.writeResponse(httpResponse);
        }
        finally {
            if (!socket.isClosed()) {
                try {
                    os.flush();

                    is.close();
                    os.close();

                    socket.close();
                }
                catch (IOException ex) {
                    LOGGER.error("Could not flush/close socket.", ex);
                }
            }
        }
    }

    private void clearBuffer() {
        /* zero out the buffer from last time */
        for (int i = 0; i < BUF_SIZE; i++) {
            buf[i] = 0;
        }
    }

    private IHttpContext establishContext(InputStream is, InetAddress clientAddress, OutputStream os) throws IOException, HttpException {
        try {
            IHttpRequest httpRequest = parseHttpRequest(is);
            httpRequest.setInputStream(is);

            try {
                httpRequest.setClientHostAddress(clientAddress.getHostAddress());
                httpRequest.setClientHostName(clientAddress.getHostName());
            } catch (Exception ex) {
                LOGGER.warn("Could not determine client ip/hostname.", ex);
            }
            
            IHttpResponse httpResponse = createHttpResponseFromHttpRequest(httpRequest);
            httpResponse.setOutputStream(os);

            return new HttpContext(httpRequest, httpResponse);
        }
        catch (Exception ex) {            throw new HttpException(HttpStatusCode.STATUS_400_BAD_REQUEST, "Your HTTP client's request ended unexpectedly.");
        }
    }

    private IHttpRequest parseHttpRequest(InputStream is) {
        IHttpRequestParser httpParser = new HttpRequestParser();

        return httpParser.parseRequest(is);
    }

    private IHttpResponse createHttpResponseForStatusCode(IHttpStatusCode statusCode) {
        IHttpResponse httpResponse = new HttpResponse(HttpVersion.HTTP_11, statusCode);

        addDefaultHeadersToResponse(httpResponse);

        return httpResponse;
    }

    private IHttpResponse createHttpResponseFromHttpRequest(IHttpRequest httpRequest) {
        IHttpResponse httpResponse;

        if (httpRequest.getVersion().isHTTP11()) {
            httpResponse = new HttpResponse(HttpVersion.HTTP_11);
            httpResponse.setConnectionType(ConnectionType.KEEP_ALIVE);
            httpResponse.getHeaders().addHeader(new StringHttpHeader("Keep-Alive", "timeout=5, max=100"));
        } else {
            httpResponse = new HttpResponse(HttpVersion.HTTP_10);
            httpResponse.setConnectionType(ConnectionType.CLOSE);
        }

        addDefaultHeadersToResponse(httpResponse);

        return httpResponse;
    }

    private void addDefaultHeadersToResponse(IHttpResponse httpResponse) {
        SimpleDateFormat formatter = new SimpleDateFormat("EEE, dd MMM yyyy hh:mm:ss zzz", Locale.US);
        formatter.setTimeZone(TimeZone.getTimeZone("GMT"));
        Date date = new Date();

        httpResponse.setVersion(HttpVersion.HTTP_11);
        httpResponse.getHeaders().addHeader(new StringHttpHeader("Date", formatter.format(date)));
        httpResponse.getHeaders().addHeader(new StringHttpHeader("Connection", "close"));
    }

    private void handleRequest(IHttpRequest request) {
        for (IHttpRequestHandler handler : requestHandlers) {
            handler.handleRequest(request);
        }
    }

    private void handleContext(IHttpContext context) {
        if (contextHandler != null) {
            contextHandler.handleContext(context);
        }
    }

    public void addRequestHandler(IHttpRequestHandler requestHandler) {
        requestHandlers.add(requestHandler);
    }

    public void setContextHandler(IHttpContextHandler contextHandler) {
        this.contextHandler = contextHandler;
    }
    
    public void setMaximumRequestLengthInBytes(long value) {
        this.maximumRequestLengthInBytes = value;
    }
    
    public final long getMaximumRequestLengthInBytes() {
        return this.maximumRequestLengthInBytes;
    }
    
}
