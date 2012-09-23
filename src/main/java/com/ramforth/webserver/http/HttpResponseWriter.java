/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ramforth.webserver.http;

import java.io.OutputStream;
import java.io.PrintStream;

/**
 *
 * @author Tobias Ramforth <tobias.ramforth at tu-dortmund.de>
 */
public class HttpResponseWriter implements IHttpResponseWriter {

    public static final String SP = " ";
    public static final String CR = "\r";
    public static final String LF = "\n";
    public static final String EOL = CR + LF;
    private OutputStream outputStream;

    public HttpResponseWriter(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    @Override
    public void writeResponse(IHttpResponse httpResponse) {
        PrintStream printStream = new PrintStream(outputStream);

        writeStatusLine(printStream, httpResponse);

        writeHeaders(printStream, httpResponse.getHeaders());

        writeBody(printStream, httpResponse);
    }

    private void writeStatusLine(PrintStream printStream, IHttpResponse httpResponse) {
        String statusLine = String.format("%1s %2s %3s%4s", httpResponse.getVersion().toString(), httpResponse.getStatusCode().getCode(), httpResponse.getStatusCode().getReason(), EOL);
        printStream.print(statusLine);
    }

    private void writeHeaders(PrintStream printStream, IHttpHeaders httpResponseHeaders) {
        for (IHttpHeader httpResponseHeader : httpResponseHeaders) {
            writeHeader(printStream, httpResponseHeader);
        }
        printStream.print(EOL);
    }

    private void writeHeader(PrintStream printStream, IHttpHeader httpResponseHeader) {
        printStream.print(String.format("%1s: %2s%3s", httpResponseHeader.getName(), httpResponseHeader.getRawValue(), EOL));
    }

    private void writeBody(PrintStream printStream, IHttpResponse httpResponse) {
        //if (httpResponse.) {
        //}
    }
}
