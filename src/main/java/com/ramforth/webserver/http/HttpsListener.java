/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ramforth.webserver.http;

import java.net.InetAddress;
import javax.net.ssl.SSLServerSocketFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Tobias Ramforth <tobias.ramforth at tu-dortmund.de>
 */
public class HttpsListener extends AbstractHttpListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(HttpsListener.class);

    static {
        serverSocketFactory = SSLServerSocketFactory.getDefault();
    }

    public HttpsListener(int port) {
        super(port);
    }

    public HttpsListener(InetAddress listenAddress, int port) {
        super(listenAddress, port);
    }

}
