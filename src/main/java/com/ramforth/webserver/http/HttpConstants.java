package com.ramforth.webserver.http;

public interface HttpConstants {

	/** 2XX: generally "OK" */
	public static final int HTTP_OK = 200;
	public static final int HTTP_CREATED = 201;
	public static final int HTTP_ACCEPTED = 202;
	public static final int HTTP_NON_AUTHORITATIVE_INFORMATION = 203;
	public static final int HTTP_NO_CONTENT = 204;
	public static final int HTTP_RESET_CONTENT = 205;
	public static final int HTTP_PARTIAL_CONTENT = 206;

	/** 3XX: relocation/redirect */
	public static final int HTTP_MULTIPLE_CHOICES = 300;
	public static final int HTTP_MOVED_PERMANENTLY = 301;
	public static final int HTTP_FOUND = 302;
	public static final int HTTP_SEE_OTHER = 303;
	public static final int HTTP_NOT_MODIFIED = 304;
	public static final int HTTP_USE_PROXY = 305;
	// public static final int ???? = 306; //unused
	public static final int HTTP_TEMPORARY_REDIRECT = 307;

	/** 4XX: client error */
	public static final int HTTP_BAD_REQUEST = 400;
	public static final int HTTP_UNAUTHORIZED = 401;
	public static final int HTTP_PAYMENT_REQUIRED = 402;
	public static final int HTTP_FORBIDDEN = 403;
	public static final int HTTP_NOT_FOUND = 404;
	public static final int HTTP_METHOD_NOT_ALLOWED = 405;
	public static final int HTTP_NOT_ACCEPTABLE = 406;
	public static final int HTTP_PROXY_AUTHENTICATION_REQUIRED = 407;
	public static final int HTTP_REQUEST_TIMEOUT = 408;
	public static final int HTTP_CONFLICT = 409;
	public static final int HTTP_GONE = 410;
	public static final int HTTP_LENGTH_REQUIRED = 411;
	public static final int HTTP_PRECONDITION_FAILED = 412;
	public static final int HTTP_REQUEST_ENTITY_TOO_LARGE = 413;
	public static final int HTTP_REQUEST_URI_TOO_LONG = 414;
	public static final int HTTP_UNSUPPORTED_MEDIA_TYPE = 415;
	public static final int HTTP_REQUESTED_RANGE_NOT_SATISFIABLE = 416;
	public static final int HTTP_EXPECTATION_FAILED = 417;

	/** 5XX: server error */
	public static final int HTTP_INTERNAL_SERVER_ERROR = 500;
	public static final int HTTP_NOT_IMPLEMENTED = 501;
	public static final int HTTP_BAD_GATEWAY = 502;
	public static final int HTTP_SERVICE_UNAVAILABLE = 503;
	public static final int HTTP_GATEWAY_TIMEOUT = 504;
	public static final int HTTP_VERSION_NOT_SUPPORTED = 505;

}
