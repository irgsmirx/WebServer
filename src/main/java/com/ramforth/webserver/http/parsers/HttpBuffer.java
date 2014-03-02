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
package com.ramforth.webserver.http.parsers;

public class HttpBuffer {

    private static final int INITIAL_CAPACITY = 2048;
    protected byte[] buf = null;
    protected int length = 0;

    public HttpBuffer() {
        buf = new byte[INITIAL_CAPACITY];
    }

    public HttpBuffer(int capacity) {
        buf = new byte[capacity];
    }

    public HttpBuffer append(byte b) {
        int newLength = length + 1;
        if (newLength > buf.length) {
            expandCapacity(newLength);
        }
        buf[length++] = b;
        return this;
    }

    public HttpBuffer append(int i) {
        return append((byte) i);
    }

    public void ensureCapacity(int minimumCapacity) {
        if (minimumCapacity > buf.length) {
            expandCapacity(minimumCapacity);
        }
    }

    protected void expandCapacity(int minimumCapacity) {
        int newCapacity = (buf.length + 1) * 2;
        if (newCapacity < 0) {
            newCapacity = Integer.MAX_VALUE;
        } else if (minimumCapacity > newCapacity) {
            newCapacity = minimumCapacity;
        }
        byte newBuf[] = new byte[newCapacity];
        System.arraycopy(buf, 0, newBuf, 0, length);
        buf = newBuf;
    }

    public void trim() {
        if (length < buf.length) {
            byte[] newBuf = new byte[length];
            System.arraycopy(buf, 0, newBuf, 0, length);
            this.buf = newBuf;
        }
    }

    public byte[] getCopy() {
        byte bufCopy[] = new byte[length];
        System.arraycopy(buf, 0, bufCopy, 0, length);
        return bufCopy;
    }

    public void reset() {
        length = 0;
    }

    @Override
    public String toString() {
        return new String(getCopy());
    }
}
