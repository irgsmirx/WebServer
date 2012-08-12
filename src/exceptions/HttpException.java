package exceptions;

import http.HttpError;
import http.IHttpStatusCode;

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
	 * @param errorCode
	 *          The errorCode to set.
	 */
	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

  public IHttpStatusCode getStatusCode() {
    return statusCode;
  }
  
}
