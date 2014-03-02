 /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ramforth.webserver.http.parsers;

import java.io.IOException;
import java.io.InputStream;

/**
 *
 * @author tobias
 */
public class BoundaryDelimitedInputStream extends InputStream {

    private static final int EOF = -1;
    private static final int SKIP_BUFFER_SIZE = 4096;

    private final InputStream wrappedInputStream;
    private final byte[] boundaryBytes;
    private final int boundaryLength;
    private long bytesRead = 0;
    private int currentBoundaryPosition = 0;

    private byte[] buffer;
    private int currentBufferIndex = 0;
    private int bufferEndIndex = 0;

    private boolean reachedBoundary = false;

    public BoundaryDelimitedInputStream(InputStream wrappedInputStream, byte[] boundaryBytes) {
        this.wrappedInputStream = wrappedInputStream;
        this.boundaryBytes = new byte[boundaryBytes.length + 2];
        System.arraycopy(boundaryBytes, 0, this.boundaryBytes, 2, boundaryBytes.length);
        this.boundaryBytes[0] = '\r';
        this.boundaryBytes[1] = '\n';
        this.boundaryLength = this.boundaryBytes.length;

        this.buffer = new byte[this.boundaryLength];
    }

    @Override
    public int read() throws IOException {
        int b = -1;

        if (reachedBoundary) {
            return b;
        }

        if (currentBufferIndex < bufferEndIndex) {
            b = buffer[currentBufferIndex];
            currentBufferIndex++;
        } else {
            currentBufferIndex = 0;
            bufferEndIndex = 0;
            currentBoundaryPosition = 0;

            while ((b = wrappedInputStream.read()) != -1) {
                buffer[bufferEndIndex] = (byte) b;
                bufferEndIndex++;

                if (b == boundaryBytes[currentBoundaryPosition]) {
                    if (currentBoundaryPosition == boundaryLength - 1) {
                        reachedBoundary = true;
                        return BoundaryDelimitedInputStream.EOF;
                    } else {
                        currentBoundaryPosition++;
                    }
                } else {
                    currentBoundaryPosition = 0;
                    break;
                }
            }

            if (b == -1) {
                return BoundaryDelimitedInputStream.EOF;
            }

            if (bufferEndIndex > 0) {
                b = buffer[currentBufferIndex] & 0xff;
                currentBufferIndex++;
            }
        }

        return b;
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        int totalBytesRead = 0;

        for (int i = 0; i < len; i++) {
            int readByte = read();
            if (readByte == -1) {
                if (totalBytesRead > 0) {
                    break;
                } else {
                    return BoundaryDelimitedInputStream.EOF;
                }
            }
            b[off + totalBytesRead] = (byte) readByte;
            totalBytesRead++;
        }

        return totalBytesRead;
    }

    @Override
    public int read(byte[] b) throws IOException {
        return read(b, 0, b.length);
    }

    @Override
    public boolean markSupported() {
        return wrappedInputStream.markSupported();
    }

    @Override
    public synchronized void reset() throws IOException {
        wrappedInputStream.reset();
    }

    @Override
    public synchronized void mark(int readlimit) {
        wrappedInputStream.mark(readlimit);
    }

    @Override
    public void close() throws IOException {
        wrappedInputStream.close();
    }

    @Override
    public int available() throws IOException {
        return 0;//Math.min(wrappedInputStream.available(), (int) (this.maximumLength - this.bytesRead));
    }

    @Override
    public long skip(long n) throws IOException {
        if (n <= 0) {
            return 0;
        }

        long stillSkippableBytes = 0;//Math.min(n, (this.maximumLength - this.bytesRead));

        byte[] skipBuffer = new byte[SKIP_BUFFER_SIZE];

        long skippedBytes = 0;
        while (stillSkippableBytes > 0) {
            int couldRead = read(skipBuffer, 0, SKIP_BUFFER_SIZE);

            if (couldRead == BoundaryDelimitedInputStream.EOF) {
                break;
            }

            stillSkippableBytes -= couldRead;
            skippedBytes += couldRead;
        }

        return skippedBytes;
    }

}
