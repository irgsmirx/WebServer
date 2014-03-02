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

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.ramforth.webserver.http.HttpRequestJsonBodyData;
import com.ramforth.webserver.http.IHttpRequestBodyData;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

/**
 *
 * @author Tobias Ramforth <tobias.ramforth at tu-dortmund.de>
 */
public class HttpRequestApplicationJsonBodyParser extends AbstractHttpRequestBodyParser {

    @Override
    public IHttpRequestBodyData parse(InputStream inputStream) {
        JsonParser jsonParser = new JsonParser();
        Reader reader = new InputStreamReader(inputStream, contentType.getCharset());
        JsonObject jsonObject = (JsonObject) jsonParser.parse(reader);

        return new HttpRequestJsonBodyData(jsonObject);
    }
}
