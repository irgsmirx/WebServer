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
