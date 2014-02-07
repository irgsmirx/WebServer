/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ramforth.webserver.http.parsers;

import com.ramforth.webserver.http.IHttpRequestBodyData;
import com.ramforth.webserver.http.headers.entity.ContentLengthHttpHeader;
import com.ramforth.webserver.http.headers.entity.ContentTypeHttpHeader;
import com.ramforth.webserver.http.headers.general.TransferEncodingHttpHeader;
import java.io.InputStream;

/**
 *
 * @author Tobias Ramforth <tobias.ramforth at tu-dortmund.de>
 */
public interface IHttpRequestBodyParser {

    void setTransferEncoding(TransferEncodingHttpHeader transferEncoding);
    void setContentType(ContentTypeHttpHeader contentType);
    IHttpRequestBodyData parse(InputStream inputStream);
}
