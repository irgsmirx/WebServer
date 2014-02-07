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
public class ContentLengthInputStream extends InputStream {

    private static final int EOF = -1;
    private static final int SKIP_BUFFER_SIZE = 4096;
    
    private final InputStream wrappedInputStream;
    private final long contentLength;
    private long bytesRead = 0;
    
    
    public ContentLengthInputStream(InputStream wrappedInputStream, long contentLength) {
        this.wrappedInputStream = wrappedInputStream;
        this.contentLength = contentLength;
    }
    
    private boolean contentLengthReached() {
        return (bytesRead >= contentLength);
    }
    
    @Override
    public int read() throws IOException {
        if (contentLengthReached()) {
            return ContentLengthInputStream.EOF;
        }
        
        int b = wrappedInputStream.read();
        
        if (b == ContentLengthInputStream.EOF) {
            if (!contentLengthReached()) {
                throw new RuntimeException("Unexpected end of input at byte " + this.bytesRead + ". Expected " + (this.contentLength - this.bytesRead) + " more bytes!");
            }
        }
        
        if (b != ContentLengthInputStream.EOF) {
            bytesRead++;
        }
        return b;
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        if (contentLengthReached()) {
            return ContentLengthInputStream.EOF;
        }
        
        if ((this.bytesRead + len) > this.contentLength) {
            len = (int)(this.contentLength - this.bytesRead);
        }
        
        int currentBytesRead = this.wrappedInputStream.read(b, off, len);
        
        if (currentBytesRead == ContentLengthInputStream.EOF && !contentLengthReached()) {
            throw new RuntimeException("Unexpected end of input at byte " + this.bytesRead + ". Expected " + (this.contentLength - this.bytesRead) + " more bytes!");
        }
        
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
        return Math.min(wrappedInputStream.available(), (int) (this.contentLength - this.bytesRead));
    }

    @Override
    public long skip(long n) throws IOException {
        if (n <= 0) {
            return 0;
        }
        
        long stillSkippableBytes = Math.min(n, (this.contentLength - this.bytesRead));
        
        byte[] skipBuffer = new byte[SKIP_BUFFER_SIZE];
        
        long skippedBytes = 0;
        while (stillSkippableBytes > 0) {
            int couldRead = read(skipBuffer, 0, SKIP_BUFFER_SIZE);
            
            if (couldRead == ContentLengthInputStream.EOF) {
                break;
            }
            
            stillSkippableBytes -= couldRead;
            skippedBytes += couldRead;
        }     
        
        
        return skippedBytes;
    }
    
}
