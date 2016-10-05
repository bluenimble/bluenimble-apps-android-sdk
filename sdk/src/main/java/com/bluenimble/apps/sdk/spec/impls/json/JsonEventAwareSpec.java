package com.bluenimble.apps.sdk.spec.impls.json;

import java.util.Iterator;

import com.bluenimble.apps.sdk.Json;
import com.bluenimble.apps.sdk.json.JsonObject;
import com.bluenimble.apps.sdk.spec.EventAwareSpec;

public class JsonEventAwareSpec implements EventAwareSpec {

	private static final long serialVersionUID = 3120769228088536157L;
	
	protected JsonObject events;
	
	public JsonEventAwareSpec () {
	}

	public JsonEventAwareSpec (JsonObject events) {
		this.events = events;
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
		return Json.getObject (events, id);
	}
	
}
