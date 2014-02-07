package com.ramforth.webserver.http.parsers;

import com.ramforth.webserver.exceptions.HttpException;
import com.ramforth.webserver.http.IHttpHeaders;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

public class ChunkedInputStream extends FilterInputStream {

    private static final byte CR = '\r';
    private static final byte LF = '\n';
    protected boolean readingChunkHeader = true;
    protected boolean endOfStream = false;
    protected IHttpHeaders headers = null;
    protected int currentChunkSize = -1;
    protected int alreadyReadFromChunk = 0;

    public ChunkedInputStream(InputStream is, IHttpHeaders headers) {
        super(is);
        this.headers = headers;
        readingChunkHeader = true;
        endOfStream = false;
        currentChunkSize = -1;
        alreadyReadFromChunk = 0;
    }

    @Override
    public int read() throws IOException {
        byte[] buf = new byte[1];
        int length = read(buf);
        if (length == -1) {
            return length;
        } else {
            return buf[0] & 0xff;
        }
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        if (endOfStream) {
            return -1;
        } else {
            if (readingChunkHeader) {
                HttpBuffer buffer = new HttpBuffer();
                boolean readingChunkExtension = false;

                int ch = in.read();
                int last = -1;

                while (true) {
                    if (ch == -1) {
                        break;
                    } else if (ch == LF) {
                        if (last == CR) {
                            try {
                                String size = buffer.toString();
                                currentChunkSize = Integer.parseInt(size, 16);
                            }
                            catch (NumberFormatException e) {
                                // error
                            }
                            break;
                        } else {
                        }
                    } else if (ch == ';') {
                        readingChunkExtension = true;
                    } else if (isHEX(ch)) {
                        if (readingChunkExtension) {
                        } else {
                            buffer.append(ch);
                        }
                    } else {
                        if (readingChunkExtension) {
                        } else {
                            // error
                        }
                    }
                    last = ch;
                    ch = in.read();
                }
                readingChunkHeader = false;
                readingChunkExtension = false;
                alreadyReadFromChunk = 0;
            }

            if (currentChunkSize == 0) {
                // last chunk, read headers
                try {
                    IHttpHeadersParser headersParser = new HttpHeadersParser();
                    headers = headersParser.parse(in);
                }
                catch (HttpException e) {
                    throw new IOException(e.getMessage());
                }
                endOfStream = true;
                return -1;
            } else {
                // chunk in between
                int readableBytes = Math.min(currentChunkSize - alreadyReadFromChunk, len);
                int length = in.read(b, off, readableBytes);

                if (length == -1) {
                    throw new IOException("Your HTTP client's request ended unexpectedly.");
                } else {
                    alreadyReadFromChunk += length;

                    if (alreadyReadFromChunk == currentChunkSize) {
                        // done with reading
                        int ch = in.read();
                        if (ch == '\r') {
                            ch = in.read();
                            if (ch == '\n') {
                                readingChunkHeader = true;
                                return length;
                            } else {
                                throw new IOException("Your HTTP client's request ended unexpectedly.");
                            }
                        } else if (ch == -1) {
                            endOfStream = true;
                            return -1;
                        } else {
                            throw new IOException("Your HTTP client's request contained data after end of chunk.");
                        }
                    } else {
                        // still reading
                        return length;
                    }
                }
            }
        }
    }

    @Override
    public int available() throws IOException {
        if (readingChunkHeader) {
            return 0;
        } else {
            return currentChunkSize;
        }
    }

    @Override
    public void close() throws IOException {
        in.close();
    }

    public static boolean isDIGIT(int ch) {
        return ( ch >= 48 && ch <= 57 );
    }

    protected boolean isHEX(int ch) {
        return ( ch == 'a' || ch == 'b' || ch == 'c' || ch == 'd' || ch == 'e' || ch == 'f' || ch == 'A' || ch == 'B'
                || ch == 'C' || ch == 'D' || ch == 'E' || ch == 'F' || isDIGIT(ch) );
    }
}
