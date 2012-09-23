/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ramforth.webserver.http;

/**
 *
 * @author Tobias Ramforth <tobias.ramforth at tu-dortmund.de>
 */
public class HttpStatusCode implements IHttpStatusCode {

  public static final IHttpStatusCode STATUS_100_CONTINUE = new HttpStatusCode(HttpStatusCodeClass.INFORMATIONAL, 100, "Continue");
  public static final IHttpStatusCode STATUS_101_SWITCH_PROTOCOLS = new HttpStatusCode(HttpStatusCodeClass.INFORMATIONAL, 101, "Switching Protocols");
  
  public static final IHttpStatusCode STATUS_200_OK = new HttpStatusCode(HttpStatusCodeClass.SUCCESS, 200, "OK");
  public static final IHttpStatusCode STATUS_201_CREATED = new HttpStatusCode(HttpStatusCodeClass.SUCCESS, 201, "Created");
  public static final IHttpStatusCode STATUS_202_ACCEPTED = new HttpStatusCode(HttpStatusCodeClass.SUCCESS, 202, "Accepted");
  public static final IHttpStatusCode STATUS_203_NON_AUTHORATIVE_INFORMATION = new HttpStatusCode(HttpStatusCodeClass.SUCCESS, 203, "Non-Authoritative Information");
  public static final IHttpStatusCode STATUS_204_NO_CONTENT = new HttpStatusCode(HttpStatusCodeClass.SUCCESS, 204, "No Content");
  public static final IHttpStatusCode STATUS_205_RESET_CONTENT = new HttpStatusCode(HttpStatusCodeClass.SUCCESS, 205, "Reset Content");
  public static final IHttpStatusCode STATUS_206_PARTIAL_CONTENT = new HttpStatusCode(HttpStatusCodeClass.SUCCESS, 206, "Partial Content");
  
  public static final IHttpStatusCode STATUS_300_MULTIPLE_CHOICES = new HttpStatusCode(HttpStatusCodeClass.REDIRECTION, 300, "Multiple Choices");
  public static final IHttpStatusCode STATUS_301_MOVED_PERMANENTLY = new HttpStatusCode(HttpStatusCodeClass.REDIRECTION, 301, "Moved Permanently");
  public static final IHttpStatusCode STATUS_302_FOUND = new HttpStatusCode(HttpStatusCodeClass.REDIRECTION, 302, "Found");
  public static final IHttpStatusCode STATUS_303_SEE_OTHER = new HttpStatusCode(HttpStatusCodeClass.REDIRECTION, 303, "See Other");
  public static final IHttpStatusCode STATUS_304_NOT_MODIFIED = new HttpStatusCode(HttpStatusCodeClass.REDIRECTION, 304, "Not Modified");
  public static final IHttpStatusCode STATUS_305_USE_PROXY = new HttpStatusCode(HttpStatusCodeClass.REDIRECTION, 305, "Use Proxy");
  public static final IHttpStatusCode STATUS_307_TEMPORARY_REDIRECT = new HttpStatusCode(HttpStatusCodeClass.REDIRECTION, 307, "Temporary Redirect");
  
  public static final IHttpStatusCode STATUS_400_BAD_REQUEST = new HttpStatusCode(HttpStatusCodeClass.CLIENT_ERROR, 400, "Bad Request");
  public static final IHttpStatusCode STATUS_401_UNAUTHORIZED = new HttpStatusCode(HttpStatusCodeClass.CLIENT_ERROR, 401, "Unauthorized");
  public static final IHttpStatusCode STATUS_402_PAYMENT_REQUIRED = new HttpStatusCode(HttpStatusCodeClass.CLIENT_ERROR, 402, "Payment Required");
  public static final IHttpStatusCode STATUS_403_FORBIDDEN = new HttpStatusCode(HttpStatusCodeClass.CLIENT_ERROR, 403, "Forbidden");
  public static final IHttpStatusCode STATUS_404_NOT_FOUND = new HttpStatusCode(HttpStatusCodeClass.CLIENT_ERROR, 404, "Not Found");
  public static final IHttpStatusCode STATUS_405_METHOD_NOT_ALLOWED = new HttpStatusCode(HttpStatusCodeClass.CLIENT_ERROR, 405, "Method Not Allowed");
  public static final IHttpStatusCode STATUS_406_NOT_ACCEPTABLE = new HttpStatusCode(HttpStatusCodeClass.CLIENT_ERROR, 406, "Not Acceptable");
  public static final IHttpStatusCode STATUS_407_PROXY_AUTHENTICATION_REQUIRED = new HttpStatusCode(HttpStatusCodeClass.CLIENT_ERROR, 407, "Proxy Authentication Required");
  public static final IHttpStatusCode STATUS_408_REQUEST_TIMEOUT = new HttpStatusCode(HttpStatusCodeClass.CLIENT_ERROR, 408, "Request Time-out");
  public static final IHttpStatusCode STATUS_409_CONFLICT = new HttpStatusCode(HttpStatusCodeClass.CLIENT_ERROR, 409, "Conflict");
  public static final IHttpStatusCode STATUS_410_GONE = new HttpStatusCode(HttpStatusCodeClass.CLIENT_ERROR, 410, "Gone");
  public static final IHttpStatusCode STATUS_411_LENGTH_REQUIRED = new HttpStatusCode(HttpStatusCodeClass.CLIENT_ERROR, 411, "Length Required");
  public static final IHttpStatusCode STATUS_412_PRECONDITION_FAILED = new HttpStatusCode(HttpStatusCodeClass.CLIENT_ERROR, 412, "Precondition Failed");
  public static final IHttpStatusCode STATUS_413_REQUEST_ENTITY_TOO_LARGE = new HttpStatusCode(HttpStatusCodeClass.CLIENT_ERROR, 413, "Request Entity Too Large");
  public static final IHttpStatusCode STATUS_414_REQUEST_URI_TOO_LARGE = new HttpStatusCode(HttpStatusCodeClass.CLIENT_ERROR, 414, "Request-URI Too Large");
  public static final IHttpStatusCode STATUS_415_UNSUPPORTED_MEDIA_TYPE = new HttpStatusCode(HttpStatusCodeClass.CLIENT_ERROR, 415, "Unsupported Media Type");
  public static final IHttpStatusCode STATUS_416_REQUESTED_RANGE_NOT_SATISFIABLE = new HttpStatusCode(HttpStatusCodeClass.CLIENT_ERROR, 416, "Requested range not satisfiable");
  public static final IHttpStatusCode STATUS_417_EXPECTATION_FAILED = new HttpStatusCode(HttpStatusCodeClass.CLIENT_ERROR, 417, "Expectation Failed");
  
  public static final IHttpStatusCode STATUS_500_INTERNAL_SERVER_ERROR = new HttpStatusCode(HttpStatusCodeClass.SERVER_ERROR, 500, "Internal Server Error");
  public static final IHttpStatusCode STATUS_501_NOT_IMPLEMENTED = new HttpStatusCode(HttpStatusCodeClass.SERVER_ERROR, 501, "Not Implemented");
  public static final IHttpStatusCode STATUS_502_BAD_GATEWAY = new HttpStatusCode(HttpStatusCodeClass.SERVER_ERROR, 502, "Bad Gateway");
  public static final IHttpStatusCode STATUS_503_SERVICE_UNAVAILABLE = new HttpStatusCode(HttpStatusCodeClass.SERVER_ERROR, 503, "Service Unavailable");
  public static final IHttpStatusCode STATUS_504_GATEWAY_TIMEOUT = new HttpStatusCode(HttpStatusCodeClass.SERVER_ERROR, 504, "Gateway Time-out");
  public static final IHttpStatusCode STATUS_505_HTTP_VERSION_NOT_SUPPORTED = new HttpStatusCode(HttpStatusCodeClass.SERVER_ERROR, 505, "HTTP Version not supported");
  
  
  private HttpStatusCodeClass statusCodeClass;
  private int code;
  private String reason;
  
  public HttpStatusCode(HttpStatusCodeClass statusCodeClass, int code, String reason) {
    this.statusCodeClass = statusCodeClass;
    this.code = code;
    this.reason = reason;
  }
  
  @Override
  public HttpStatusCodeClass getStatusCodeClass() {
    return statusCodeClass;
  }

  @Override
  public void setStatusCodeClass(HttpStatusCodeClass value) {
    this.statusCodeClass = value;
  }
  
  @Override
  public int getCode() {
    return code;
  }

  @Override
  public void setCode(int value) {
    this.code = value;
  }
  
  @Override
  public String getReason() {
    return reason;
  }

  @Override
  public void setReason(String value) {
    this.reason = value;
  }
  
}
