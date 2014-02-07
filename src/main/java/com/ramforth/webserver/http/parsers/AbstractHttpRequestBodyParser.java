/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ramforth.webserver.http.parsers;

import com.ramforth.webserver.http.headers.entity.ContentLengthHttpHeader;
import com.ramforth.webserver.http.headers.entity.ContentTypeHttpHeader;
import com.ramforth.webserver.http.headers.general.TransferEncodingHttpHeader;

/**
 *
 * @author tobias
 */
public abstract class AbstractHttpRequestBodyParser implements IHttpRequestBodyParser {

    protected ContentLengthHttpHeader contentLength = null;
    protected ContentTypeHttpHeader contentType = null;
    protected TransferEncodingHttpHeader transferEncoding = null;
    
    @Override
    public void setContentType(ContentTypeHttpHeader contentType) {
        this.contentType = contentType;
    }
    
    @Override
    public void setTransferEncoding(TransferEncodingHttpHeader transferEncoding) {
        this.transferEncoding = transferEncoding;
    }

}
