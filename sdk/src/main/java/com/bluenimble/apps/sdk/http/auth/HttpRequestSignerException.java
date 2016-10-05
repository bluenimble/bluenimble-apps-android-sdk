package com.bluenimble.apps.sdk.http.auth;

public class HttpRequestSignerException extends Exception {

	private static final long serialVersionUID = 1723726073941206056L;

	public HttpRequestSignerException() {
		super();
	}

	public HttpRequestSignerException (String message) {
		super(message);
	}

	public HttpRequestSignerException (Throwable e) {
		super(e);
	}

	public HttpRequestSignerException (String message, Throwable e) {
		super(message, e);
	}
}
