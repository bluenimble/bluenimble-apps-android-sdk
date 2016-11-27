package com.bluenimble.apps.sdk.backend;

import com.bluenimble.apps.sdk.json.JsonObject;

public class DataValidatorException extends Exception {

	private static final long serialVersionUID = 1165308892883189037L;
	
	private JsonObject feedback;
	
	public DataValidatorException() {
		super ();
	}

	public DataValidatorException(JsonObject feedback) {
		super ();
		this.feedback = feedback;
	}

	public DataValidatorException(String message, Throwable cause) {
		super (message, cause);
	}

	public DataValidatorException(String message) {
		super (message);
	}

	public DataValidatorException(Throwable cause) {
		super (cause);
	}
	
	public JsonObject getFeedback () {
		return feedback;
	}

}
