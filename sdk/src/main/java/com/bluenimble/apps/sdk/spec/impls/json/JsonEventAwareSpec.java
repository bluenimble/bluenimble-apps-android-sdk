package com.bluenimble.apps.sdk.spec.impls.json;

import java.util.Iterator;

import com.bluenimble.apps.sdk.Json;
import com.bluenimble.apps.sdk.Spec;
import com.bluenimble.apps.sdk.json.JsonObject;
import com.bluenimble.apps.sdk.spec.EventAwareSpec;

public class JsonEventAwareSpec implements EventAwareSpec {

	private static final long serialVersionUID = 3120769228088536157L;
	
	protected JsonObject events;

	private String defaultScope;
	
	public JsonEventAwareSpec () {
	}

	public JsonEventAwareSpec (JsonObject events, String defaultScope) {
		this.events = events;
		this.defaultScope = defaultScope;
	}

	@Override
	public Iterator<String> events () {
		if (events == null || events.isEmpty ()) {
			return null;
		}
		return events.keys ();
	}

	@Override
	public JsonObject event (String id) {
		if (events == null || events.isEmpty ()) {
			return null;
		}
		JsonObject event = Json.getObject (events, id);
		if (event == null) {
			return null;
		}
		if (!event.containsKey (Spec.page.event.Scope)) {
			event.set (Spec.page.event.Scope, defaultScope);
		}
		return event;
	}
	
}
