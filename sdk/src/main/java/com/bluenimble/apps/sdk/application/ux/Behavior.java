package com.bluenimble.apps.sdk.application.ux;

import java.io.Serializable;
import java.util.Date;

import com.bluenimble.apps.sdk.Lang;
import com.bluenimble.apps.sdk.json.JsonObject;

public class Behavior implements Serializable {

	private static final long serialVersionUID = -7202102855128259237L;

	interface Fields {
		String Timestamp 	= "timestamp";
		String Event 		= "even";
		String Subject 		= "subject";
		String Text 		= "text";
	}
	
	private Date 	timestamp;
	private String 	event;
	private String 	subject;
	private String 	text;
	
	public Behavior (String event, String subject, String text) {
		this (event, subject);
		this.text 		= text;
	}
	
	public Behavior (String event, String subject) {
		this.timestamp = new Date ();
		this.event 		= event;
		this.subject 	= subject;
	}
	
	public JsonObject toJson () {
		return (JsonObject)new JsonObject ()
				.set (Fields.Timestamp, Lang.toUTC (timestamp))
				.set (Fields.Event, event)
				.set (Fields.Subject, subject)
				.set (Fields.Text, text);
	}
	
}
