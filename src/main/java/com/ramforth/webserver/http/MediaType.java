/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ramforth.webserver.http;

import java.nio.charset.Charset;

/**
 *
 * @author Tobias Ramforth <tobias.ramforth at tu-dortmund.de>
 */
public class MediaType implements IMediaType {

    private static final String DEFAULT_MEDIA_TYPE = "application/octet-stream";

    protected String type = DEFAULT_MEDIA_TYPE;
    protected NameValueMap parameters = new NameValueMap();

    @Override
    public String getType() {
        return type;
    }

    @Override
    public void setType(String value) {
        this.type = value;
    }

    @Override
    public NameValueMap getParameters() {
        return parameters;
    }

    @Override
    public void addParameter(String name, String value) {
        parameters.add(name, value);
    }

    @Override
    public void removeParameter(String name) {
        parameters.remove(name);
    }

    @Override
    public void clearParameters() {
        parameters.clear();
    }

    @Override
    public int numberOfParameters() {
        return parameters.numberOfEntries();
    }

    @Override
    public String getValue(String name) {
        return parameters.get(name);
    }

    @Override
    public boolean containsParameter(String name) {
        return parameters.containsName(name);
    }

    @Override
    public Charset getCharset() {
        try {
            String charsetString = parameters.get("charset");
            return Charset.forName(charsetString);
        }
        catch (Exception ex) {
            return Charset.defaultCharset();
        }
    }

}
