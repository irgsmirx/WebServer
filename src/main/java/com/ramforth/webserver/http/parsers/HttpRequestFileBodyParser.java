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

import com.ramforth.webserver.http.HttpRequestByteArrayBodyData;
import com.ramforth.webserver.http.HttpRequestFileBodyData;
import com.ramforth.webserver.http.IHttpRequestBodyData;
import java.io.InputStream;

/**
 *
 * @author tobias
 */
public class HttpRequestFileBodyParser extends AbstractHttpRequestBodyParser {

    @Override
    public IHttpRequestBodyData parse(InputStream inputStream) {
        IHttpRequestBodyParser byteArrayBodyParser = new HttpRequestByteArrayBodyParser();
        byteArrayBodyParser.setContentType(contentType);
        byteArrayBodyParser.setContentDisposition(contentDisposition);
        byteArrayBodyParser.setCharset(charset);
        byteArrayBodyParser.setTransferEncoding(transferEncoding);

        HttpRequestByteArrayBodyData byteArrayBodyData = (HttpRequestByteArrayBodyData) byteArrayBodyParser.parse(inputStream);

        String filename = contentDisposition.getDispositionType().getValue("filename");
        String name = contentDisposition.getDispositionType().getValue("name");
        String mimeType = contentType.getMediaType().getType();

        return new HttpRequestFileBodyData(name, filename, mimeType, byteArrayBodyData.getData());
    }

}
