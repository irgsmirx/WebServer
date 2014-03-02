/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
