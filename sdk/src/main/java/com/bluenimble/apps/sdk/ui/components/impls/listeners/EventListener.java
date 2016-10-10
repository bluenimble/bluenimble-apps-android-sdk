package com.bluenimble.apps.sdk.ui.components.impls.listeners;

import java.io.Serializable;

import com.bluenimble.apps.sdk.json.JsonObject;

public class EventListener implements Serializable {

	private static final long serialVersionUID = 6926039907807796916L;
	
	public enum Event {
		onTextChanged,
		beforeTextChanged,
		afterTextChanged,

		select,

		swipe,

		press,
		longPress,

		move,

		markerMove,
		markerPress
	}
	
	protected Event 		event;
	protected JsonObject 	eventSpec;
	
	protected EventListener (Event event, JsonObject eventSpec) {
		this.event 		= event;
		this.eventSpec 	= eventSpec;
	}
	
}
