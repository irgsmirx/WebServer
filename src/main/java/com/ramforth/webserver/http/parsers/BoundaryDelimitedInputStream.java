/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ramforth.webserver.http.parsers;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

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
    
    private final byte[] buffer;
    private int buffered = 0;
    private int currentBufferPosition = 0;
    
    
    public BoundaryDelimitedInputStream(InputStream wrappedInputStream, byte[] boundaryBytes) {
        this.wrappedInputStream = wrappedInputStream;
        this.boundaryBytes = boundaryBytes;
        this.boundaryLength = boundaryBytes.length;
        this.buffer = new byte[this.boundaryLength];
    }
    
    @Override
    public int read() throws IOException {
        if (currentBoundaryPosition == boundaryLength - 1) {
            return BoundaryDelimitedInputStream.EOF;
        }
        
        if (buffered < boundaryLength) {
            int actuallyBuffered = wrappedInputStream.read(buffer, buffered, boundaryLength - buffered);
            if (actuallyBuffered != (boundaryLength - buffered)) {
                throw new RuntimeException("");
            }
            buffered = boundaryLength;
        } else {
            System.arraycopy(buffer, 1, buffer, 0, buffer.length - 1);
            buffer[buffer.length - 1] = (byte)wrappedInputStream.read();
        }
        
        if (Arrays.equals(buffer, boundaryBytes)) {
            return BoundaryDelimitedInputStream.EOF;
        }
        
        int b = buffer[currentBufferPosition];
        currentBufferPosition++;

        if (b == BoundaryDelimitedInputStream.EOF) {
            throw new RuntimeException("Unexpected end of input at byte " + this.bytesRead + ".");
        }
        
        if (boundaryBytes[currentBoundaryPosition] == b) {
            currentBoundaryPosition++;
        } else {
            currentBoundaryPosition = 0;
        }
        
        bytesRead++;
  
        return b;
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        if (currentBoundaryPosition == boundaryLength - 1) {
            return BoundaryDelimitedInputStream.EOF;
        }

        //if ((this.bytesRead + len) > this.maximumLength) {
            //len = (int)(this.maximumLength - this.bytesRead);
        //}
        
        int currentBytesRead = this.wrappedInputStream.read(b, off, len);
        
        //if (currentBytesRead == BoundaryDelimitedInputStream.EOF && !maximumLengthReached()) {
        //    throw new RuntimeException("Unexpected end of input at byte " + this.bytesRead + ". Expected " + (this.maximumLength - this.bytesRead) + " more bytes!");
        //}
        
        if (currentBytesRead > 0) {
            this.bytesRead += currentBytesRead;
        }
        
        return currentBytesRead;
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
