package http;

public class HttpException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int errorCode;

	public HttpException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public HttpException(int errorCode) {
		super();
		this.errorCode = errorCode;
		// TODO Auto-generated constructor stub
	}

	public HttpException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public HttpException(int errorCode, String message) {
		super(message);
		this.errorCode = errorCode;
		// TODO Auto-generated constructor stub
	}

	public HttpException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public HttpException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
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

}
