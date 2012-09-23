/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ramforth.webserver.http;

/**
 *
 * @author Tobias Ramforth <tobias.ramforth at tu-dortmund.de>
 */
public class HttpVersion implements IHttpVersion {

    public static final IHttpVersion HTTP_10 = new HttpVersion(1, 0);
    public static final IHttpVersion HTTP_11 = new HttpVersion(1, 1);
    private int major;
    private int minor;

    public HttpVersion(int major, int minor) {
        this.major = major;
        this.minor = minor;
    }

    @Override
    public int getMajor() {
        return major;
    }

    @Override
    public void setMajor(int value) {
        this.major = value;
    }

    @Override
    public int getMinor() {
        return minor;
    }

    @Override
    public void setMinor(int value) {
        this.minor = value;
    }

    @Override
    public boolean isHTTP10() {
        return major == 1 && minor == 0;
    }

    @Override
    public boolean isHTTP11() {
        return major == 1 && minor == 1;
    }

    @Override
    public String toString() {
        return String.format("HTTP/%1s.%1s", major, minor);
    }
}
