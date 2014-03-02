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
