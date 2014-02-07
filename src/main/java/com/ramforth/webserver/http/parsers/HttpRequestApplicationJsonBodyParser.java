/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
        JsonObject jsonObject = (JsonObject)jsonParser.parse(reader);
        
        return new HttpRequestJsonBodyData(jsonObject);
    }
}
