/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ramforth.webserver.http;

import java.net.InetAddress;
import javax.net.ServerSocketFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Tobias Ramforth <tobias.ramforth at tu-dortmund.de>
 */
public class HttpListener extends AbstractHttpListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(HttpListener.class);

    static {
        serverSocketFactory = ServerSocketFactory.getDefault();
    }

    public HttpListener(int port) {
        super(port);
    }

    public HttpListener(InetAddress listenAddress, int port) {
        super(listenAddress, port);
    }

}
