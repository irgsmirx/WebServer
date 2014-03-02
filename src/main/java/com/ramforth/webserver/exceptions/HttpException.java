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
package com.ramforth.webserver.exceptions;

import com.ramforth.webserver.http.IHttpStatusCode;

public class HttpException extends RuntimeException {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private int errorCode;
    private IHttpStatusCode statusCode;

    public HttpException() {
        super();
    }

    public HttpException(int errorCode) {
        super();
        this.errorCode = errorCode;
    }

    public HttpException(String message) {
        super(message);
    }

    public HttpException(int errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public HttpException(String message, Throwable cause) {
        super(message, cause);
    }

    public HttpException(Throwable cause) {
        super(cause);
    }

    public HttpException(IHttpStatusCode statusCode) {
        this.statusCode = statusCode;
    }

    public HttpException(IHttpStatusCode statusCode, String message) {
        super(message);
        this.statusCode = statusCode;
    }

    /**
     * @return Returns the errorCode.
     */
    public int getErrorCode() {
        return errorCode;
    }

    /**
     * @param errorCode The errorCode to set.
     */
    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public IHttpStatusCode getStatusCode() {
        return statusCode;
    }
}
