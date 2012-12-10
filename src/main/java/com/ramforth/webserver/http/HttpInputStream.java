package com.ramforth.webserver.http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeoutException;

public class HttpInputStream extends InputStream {

    private static final Logger LOGGER = LoggerFactory.getLogger(HttpInputStream.class);
    private InputStream input;
    private long timeout;

    public HttpInputStream(InputStream input, long timeout) {
        this.input = input;
        this.timeout = timeout;
    }

    @Override
    public int available() throws IOException {
        return input.available();
    }

    @Override
    public void close() throws IOException {
        input.close();
    }

    @Override
    public void mark(int readlimit) {
        input.mark(readlimit);
    }

    @Override
    public boolean markSupported() {
        return input.markSupported();

    }

    @Override
    public int read() throws IOException {
        try {
            waitForAvailable();
        }
        catch (TimeoutException ex) {
            LOGGER.warn("Input stream timed out!", ex);
        }
        return input.read();
    }

    @Override
    public int read(byte[] b) throws IOException {
        return read(b, 0, b.length);
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        try {
            waitForAvailable();
        }
        catch (TimeoutException ex) {
            LOGGER.warn("Input stream timed out!", ex);
        }
        int n = available();
        return input.read(b, off, Math.min(len, n));
    }

    @Override
    public void reset() throws IOException {
        input.reset();
    }

    @Override
    public long skip(long n) throws IOException {
        return input.skip(n);
    }

    private void sleep(long time) {
        try {
            Thread.sleep(Math.max(0, time));
        }
        catch (InterruptedException ignore) {
        }
    }

    private void waitForAvailable() throws TimeoutException, IOException {
        long until = System.currentTimeMillis() + timeout;
        while (available() == 0) {
            if (System.currentTimeMillis() > until) {
                throw new TimeoutException("input timed out");
            }
            try {
                Thread.sleep(100);
            }
            catch (InterruptedException ignore) {
                /* do nothing */
            }
        }
    }
}
