/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ramforth.webserver.http.parsers;

import com.ramforth.webserver.http.IHttpRequest;
import com.ramforth.webserver.http.NameValueMap;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author tobias
 */
public class ParameterParser {

    private static final Logger LOGGER = LoggerFactory.getLogger(ParameterParser.class);

    public NameValueMap parse(String parameterString, boolean urlEncoded) {
        NameValueMap result = new NameValueMap();

        if (parameterString == null) {
            return result;
        }

        int queryStringLength = parameterString.length();
        for (int i = 0; i < queryStringLength; i++) {
            int startOfCurrentParameter = i;
            int indexOfEqualsSign = -1;
            while (i < queryStringLength) {
                int c = parameterString.codePointAt(i);
                if (c == '=') {
                    if (indexOfEqualsSign < 0) {
                        indexOfEqualsSign = i;
                    }
                } else {
                    if (c == '&') {
                        break;
                    }
                }
                i++;
            }

            String name = null;
            String value;
            if (indexOfEqualsSign >= 0) {
                name = parameterString.substring(startOfCurrentParameter, indexOfEqualsSign);
                value = parameterString.substring(indexOfEqualsSign + 1, i);
            } else {
                value = parameterString.substring(startOfCurrentParameter, i);
            }

            if (urlEncoded) {
                try {
                    result.add(URLDecoder.decode(name, "UTF-8"), URLDecoder.decode(value, "UTF-8"));
                }
                catch (UnsupportedEncodingException ex) {
                    LOGGER.warn("Encoding UTF-8 not supported.", ex);
                }
            } else {
                result.add(name, value);
            }

            if (i == queryStringLength - 1 && parameterString.codePointAt(i) == '&') {
                //base.Add(null, string.Empty);
            }
        }

        return result;
    }

}
