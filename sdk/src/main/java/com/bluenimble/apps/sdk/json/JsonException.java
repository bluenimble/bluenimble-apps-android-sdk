package com.bluenimble.apps.sdk.json;

public class JsonException extends Exception {

	private static final long serialVersionUID = 3778652114497335944L;
	
	private int lineNumber;
	private String lineFragment;
	
	public JsonException (int lineNumber, String lineFragment) {
		this ();
		this.lineNumber = lineNumber;
		this.lineFragment = lineFragment;
	}

	public JsonException (int lineNumber) {
		this (lineNumber, null);
	}

	public JsonException () {
		super ();
	}

	public JsonException (String message, int lineNumber, String lineFragment) {
		this (message);
		this.lineNumber = lineNumber;
		this.lineFragment = lineFragment;
	}

	public JsonException (String message) {
		super (message);
	}

	public JsonException (Throwable throwable, String message) {
		super (message, throwable);
	}

	public JsonException (Throwable throwable, String message, int lineNumber, String lineFragment) {
		this (throwable, message);
		this.lineNumber = lineNumber;
		this.lineFragment = lineFragment;
	}

	public JsonException (Throwable throwable, int lineNumber, String lineFragment) {
		this (throwable);
		this.lineNumber = lineNumber;
		this.lineFragment = lineFragment;
	}

	public JsonException (Throwable throwable) {
		super (throwable);
	}

	public int getLineNumber() {
		return lineNumber;
	}

	public void setLineNumber(int lineNumber) {
		this.lineNumber = lineNumber;
	}

	public String getLineFragment() {
		return lineFragment;
	}

	public void setLineFragment(String lineFragment) {
		this.lineFragment = lineFragment;
	}

}
