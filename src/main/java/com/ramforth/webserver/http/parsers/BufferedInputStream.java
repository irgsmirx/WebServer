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
public class BufferedInputStream extends InputStream {

    private static final int EOF = -1;
    private static final int SKIP_BUFFER_SIZE = 4096;
    private static final int DEFAULT_BUFFER_SIZE = 4096;

    private final InputStream wrappedInputStream;
    private long bytesRead = 0;

    private final int markPosition = -1;

    private final byte[] buffer;
    private final int bufferSize;
    private int currentBufferIndex;
    private int afterLastValidByteIndex;

    public BufferedInputStream(InputStream wrappedInputStream) {
        this(wrappedInputStream, BufferedInputStream.DEFAULT_BUFFER_SIZE);
    }

    public BufferedInputStream(InputStream wrappedInputStream, int bufferSize) {
        this.wrappedInputStream = wrappedInputStream;
        this.bufferSize = bufferSize;
        this.buffer = new byte[bufferSize];
    }

    private void refillBuffer() throws IOException {
        if (currentBufferIndex < afterLastValidByteIndex) {
            return;
        }

        int currentBytesRead = wrappedInputStream.read(buffer, 0, bufferSize);

        currentBufferIndex = 0;
        afterLastValidByteIndex = currentBytesRead + 1;
    }

    @Override
    public int read() throws IOException {
        refillBuffer();

        if (currentBufferIndex >= afterLastValidByteIndex) {
            return BufferedInputStream.EOF;
        }

        byte bufferedByte = buffer[currentBufferIndex];
        currentBufferIndex++;

        return bufferedByte & 0xff;
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        if (len == 0) {
            return 0;
        }

        refillBuffer();

        if (currentBufferIndex >= afterLastValidByteIndex) {
            return BufferedInputStream.EOF;
        }

        int totalBytesRead = 0;
        while (true) {
            int currentOffset = off + totalBytesRead;
            int currentLength = len - totalBytesRead;

            if (currentLength == 0) {
                break;
            }

            int currentBytesRead = numberOfCurrentlyBufferedBytes();

            if (currentBytesRead <= 0) {
                currentBytesRead = wrappedInputStream.read(buffer, currentOffset, currentLength);
                if (currentBytesRead <= 0) {
                    break;
                }
            }

            int bufferedBytes = Math.min(currentBytesRead, currentLength);

            System.arraycopy(buffer, currentBufferIndex, b, currentOffset, bufferedBytes);

            totalBytesRead += bufferedBytes;
            currentBufferIndex += bufferedBytes;
        }

        return totalBytesRead;
    }

    @Override
    public int read(byte[] b) throws IOException {
        return read(b, 0, b.length);
    }

    @Override
    public boolean markSupported() {
        return true;
    }

    @Override
    public synchronized void reset() throws IOException {
        if (markPosition < 0) {
            return;
        }
        currentBufferIndex = markPosition;
    }

    @Override
    public synchronized void mark(int readlimit) {
        wrappedInputStream.mark(readlimit);
    }

    @Override
    public void close() throws IOException {
        wrappedInputStream.close();
    }

    private int numberOfCurrentlyBufferedBytes() {
        return afterLastValidByteIndex - currentBufferIndex;
    }

    @Override
    public int available() throws IOException {
        return wrappedInputStream.available() + numberOfCurrentlyBufferedBytes();
    }

    @Override
    public long skip(long n) throws IOException {
        if (n <= 0) {
            return 0;
        }

        long currentBytesRead = numberOfCurrentlyBufferedBytes();

        if (currentBytesRead <= 0) {
            refillBuffer();

            currentBytesRead = numberOfCurrentlyBufferedBytes();
        }

        long skippableBytes = Math.min(n, currentBytesRead);

        currentBufferIndex += skippableBytes;

        return skippableBytes;
    }

}
