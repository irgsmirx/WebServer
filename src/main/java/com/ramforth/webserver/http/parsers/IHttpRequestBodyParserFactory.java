/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ramforth.webserver.http.parsers;

import com.ramforth.webserver.http.headers.entity.ContentTypeHttpHeader;
import com.ramforth.webserver.http.headers.general.ContentDispositionHttpHeader;
import com.ramforth.webserver.http.headers.general.TransferEncodingHttpHeader;

/**
 *
 * @author tobias
 */
public interface IHttpRequestBodyParserFactory {

    IHttpRequestBodyParser build(ContentTypeHttpHeader contentType, TransferEncodingHttpHeader transferEncoding);

    IHttpRequestBodyParser build(ContentTypeHttpHeader contentType, ContentDispositionHttpHeader contentDisposition);

}
