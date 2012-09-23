/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ramforth.webserver.http;

import java.net.InetAddress;

/**
 *
 * @author Tobias Ramforth <tobias.ramforth at tu-dortmund.de>
 */
public interface IHttpListener {

    void setContextHandler(IHttpContextHandler value);

    void unsetContextHandler();

//  void addModule(IHttpModule value);
//  void removeModule(IHttpModule value);
//  void clearModules();
    int getPort();

    void setPort(int value);

    int getBacklog();

    void setBacklog(int value);

    InetAddress getListenAddress();

    void setListenAddress(InetAddress value);

    void startListening();

    void stopListening();

    boolean isListening();
}
