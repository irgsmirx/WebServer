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

import com.ramforth.webserver.http.headers.entity.ContentLengthHttpHeader;
import com.ramforth.webserver.http.headers.entity.ContentTypeHttpHeader;
import com.ramforth.webserver.http.headers.general.ContentDispositionHttpHeader;
import com.ramforth.webserver.http.headers.general.TransferEncodingHttpHeader;
import java.nio.charset.Charset;

/**
 *
 * @author tobias
 */
public abstract class AbstractHttpRequestBodyParser implements IHttpRequestBodyParser {

    protected ContentLengthHttpHeader contentLength = null;
    protected ContentTypeHttpHeader contentType = null;
    protected ContentDispositionHttpHeader contentDisposition = null;
    protected TransferEncodingHttpHeader transferEncoding = null;
    protected Charset charset = Charset.defaultCharset();

    @Override
    public void setContentType(ContentTypeHttpHeader contentType) {
        this.contentType = contentType;
    }

    @Override
    public void setTransferEncoding(TransferEncodingHttpHeader transferEncoding) {
        this.transferEncoding = transferEncoding;
    }

    @Override
    public void setCharset(Charset charset) {
        this.charset = charset;
    }

    @Override
    public void setContentDisposition(ContentDispositionHttpHeader contentDisposition) {
        this.contentDisposition = contentDisposition;
    }

}
