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
