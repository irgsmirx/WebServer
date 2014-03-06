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

import com.ramforth.webserver.exceptions.HttpException;
import com.ramforth.webserver.http.parsers.HttpHeadersParser;
import com.ramforth.webserver.http.parsers.IHttpHeadersParser;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.Map;
import java.util.TreeMap;

public class HttpRequest extends HttpMessage implements IHttpRequest {

    private InputStream inputStream;
    protected HttpMethod method;
    protected URI uri;
    protected HttpBrowserCapabilities browser;
    protected String contentEncoding;
    protected String contentType;
    protected byte[] body;
    protected NameValueMap queryString = new NameValueMap();
    protected NameValueMap form = new NameValueMap();
    protected Map<String, HttpPostedFile> postedFiles = new TreeMap<>();
    protected NameValueMap serverVariables = new NameValueMap();
    protected URI urlReferrer;
    protected IHttpVersion version;
    protected String userAgent;
    protected String[] userLanguages;
    protected String clientHostAddress;
    protected String clientHostName;
    
    public HttpRequest() {
        method = HttpMethod.GET;
        uri = null;

        headers = new HttpHeaders();
    }

    public HttpRequest(InputStream is) throws IOException, HttpException {
        IHttpHeadersParser headersParser = new HttpHeadersParser();
        this.headers = headersParser.parse(is);
    }

    public HttpRequest(IHttpHeaders httpHeaders) {
        this.headers = httpHeaders;
    }

    protected static int contains(String[] array, String value) {
        for (int i = 0; i < array.length; i++) {
            if (array[i].compareTo(value) == 0) {
                return i;
            }
        }
        return -1;
    }

    /**
     * @return Returns the entity_header.
     */
    public Map<String, String> getEntityHeader() {
        return entityHeader;
    }

    /**
     * @param entityHeader
     */
    public void setEntityHeader(Map<String, String> entityHeader) {
        this.entityHeader = entityHeader;
    }

    /**
     * @return Returns the general_header.
     */
    public Map<String, String> getGeneralHeader() {
        return generalHeader;
    }

    /**
     * @param generalHeader The general_header to set.
     */
    public void setGeneralHeader(Map<String, String> generalHeader) {
        this.generalHeader = generalHeader;
    }

    /**
     * @return Returns the method.
     */
    @Override
    public HttpMethod getMethod() {
        return method;
    }

    /**
     * @param value The method to set.
     */
    @Override
    public void setMethod(HttpMethod value) {
        this.method = value;
    }

    /**
     * @return Returns the query.
     */
    @Override
    public NameValueMap getQueryString() {
        return queryString;
    }

    @Override
    public String getQueryString(String key) {
        return queryString.get(key);
    }

    @Override
    public void setQueryString(String key, String value) {
        this.queryString.add(key, value);
    }

    /**
     * @return Returns the query.
     */
    @Override
    public NameValueMap getForm() {
        return form;
    }

    @Override
    public String getForm(String key) {
        return form.get(key);
    }

    @Override
    public void setForm(String key, String value) {
        this.form.add(key, value);
    }

    /**
     * @return Returns the uri.
     */
    @Override
    public URI getUri() {
        return uri;
    }

    /**
     * @param uri The uri to set.
     */
    @Override
    public void setUri(URI uri) {
        this.uri = uri;
    }

    @Override
    public IHttpVersion getVersion() {
        return version;
    }

    @Override
    public void setVersion(IHttpVersion value) {
        this.version = value;
    }

    /**
     * @return Returns the body.
     */
    public byte[] getBody() {
        return body;
    }

    /**
     * @param body The body to set.
     */
    public void setBody(byte[] body) {
        this.body = body;
    }

    /**
     * @return Returns the headers.
     */
    @Override
    public IHttpHeaders getHeaders() {
        return headers;
    }

    /**
     * @param headers The headers to set.
     */
    public void setHeaders(IHttpHeaders headers) {
        this.headers = headers;
    }

    public HttpBrowserCapabilities getBrowser() {
        return browser;
    }

    public void setBrowser(HttpBrowserCapabilities value) {
        this.browser = value;
    }

    public String getItem(String key) {
        String value = queryString.get(key);

        if (value == null) {
            value = form.get(key);
        }

        if (value == null) {
            HttpCookie cookie = cookies.get(key);
            if (cookie != null) {
                value = cookie.getValue();
            }
        }

        if (value == null) {
            value = serverVariables.get(key);
        }

        return value;
    }

    @Override
    public NameValueMap getParams() {
        NameValueMap params = queryString.union(form);

        for (HttpCookie cookie : cookies.values()) {
            params.add(cookie.getName(), cookie.getValue());
        }

        params.addAll(serverVariables);

        return params;
    }

    @Override
    public String getParams(String key) {
        return getParams().get(key);
    }

    @Override
    public String getContentType() {
        return contentType;
    }

    @Override
    public void setContentType(String value) {
        this.contentType = value;
    }

    public Map<String, HttpPostedFile> getFiles() {
        return postedFiles;
    }

    public URI getUrlReferrer() {
        return urlReferrer;
    }

    public String getUserAgent() {
        return userAgent;
    }

    @Override
    public void setClientHostAddress(String value) {
        this.clientHostAddress = value;
    }

    @Override
    public void setClientHostName(String value) {
        this.clientHostName = value;
    }
    
    @Override
    public final String getClientHostAddress() {
        return clientHostAddress;
    }

    @Override
    public final String getClientHostName() {
        return clientHostName;
    }

    public String[] getUserLanguages() {
        return userLanguages;
    }

    @Override
    public InputStream getInputStream() {
        return inputStream;
    }

    @Override
    public void setInputStream(InputStream value) {
        this.inputStream = value;
    }

    @Override
    public final Iterable<HttpPostedFile> getPostedFiles() {
        return postedFiles.values();
    }

    @Override
    public final HttpPostedFile getPostedFile(String name) {
        return postedFiles.get(name);
    }

    @Override
    public final int getNumberOfPostedFiles() {
        return postedFiles.size();
    }
    
}
