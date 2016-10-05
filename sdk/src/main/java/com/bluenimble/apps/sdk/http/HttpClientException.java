package com.bluenimble.apps.sdk.http;

public class HttpClientException extends Exception {

	private static final long serialVersionUID = 1723726073941206056L;

	public HttpClientException() {
		super();
	}

	public HttpClientException (String message) {
		super(message);
	}

	public HttpClientException (Throwable e) {
		super(e);
	}

	public HttpClientException (String message, Throwable e) {
		super(message, e);
	}
}
