package com.bluenimble.apps.sdk.http.request;

public class HttpRequestWriteException extends Exception {

	private static final long serialVersionUID = 1723726073941206056L;

	public HttpRequestWriteException() {
		super();
	}

	public HttpRequestWriteException (String message) {
		super(message);
	}

	public HttpRequestWriteException (Throwable e) {
		super(e);
	}

	public HttpRequestWriteException (String message, Throwable e) {
		super(message, e);
	}
}
