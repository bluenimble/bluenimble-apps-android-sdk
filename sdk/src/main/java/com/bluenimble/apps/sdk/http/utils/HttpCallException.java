package com.bluenimble.apps.sdk.http.utils;

import java.io.InputStream;

public class HttpCallException extends Exception {

	private static final long serialVersionUID = 1165308892883189037L;
	
	private int status;
	private InputStream response;

	public HttpCallException () {
		super ();
	}

	public HttpCallException (int status, InputStream response) {
		super ();
		this.status = status;
		this.response = response;
	}

	public HttpCallException (String message, Throwable cause) {
		super (message, cause);
	}

	public HttpCallException (String message) {
		super (message);
	}

	public HttpCallException (Throwable cause) {
		super (cause);
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public InputStream getResponse() {
		return response;
	}

	public void setResponse(InputStream response) {
		this.response = response;
	}	

}
