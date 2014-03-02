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
import com.ramforth.webserver.http.HttpRequestFormBodyData;
import com.ramforth.webserver.http.IHttpRequestBodyData;
import com.ramforth.webserver.http.NameValueMap;
import java.io.InputStream;

/**
 *
 * @author Tobias Ramforth <tobias.ramforth at tu-dortmund.de>
 */
public class HttpRequestWwwFormUrlEncodedBodyParser extends AbstractHttpRequestBodyParser {

    private final ParameterParser parameterParser = new ParameterParser();

    @Override
    public IHttpRequestBodyData parse(InputStream inputStream) {
        IHttpRequestBodyParser byteArrayBodyParser = new HttpRequestByteArrayBodyParser();
        byteArrayBodyParser.setTransferEncoding(transferEncoding);
        byteArrayBodyParser.setContentType(contentType);
        byteArrayBodyParser.setCharset(charset);

        HttpRequestByteArrayBodyData byteArrayBodyData = (HttpRequestByteArrayBodyData) byteArrayBodyParser.parse(inputStream);

        byte[] data = byteArrayBodyData.getData();
        String parameterString = new String(data, charset);
        NameValueMap formData = parameterParser.parse(parameterString, true);
        return new HttpRequestFormBodyData(formData);
    }

}
