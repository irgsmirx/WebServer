/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ramforth.webserver.http.parsers;

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
        NameValueMap formData = parameterParser.parse(null, true);
        return new HttpRequestFormBodyData(formData);
    }
    
}
