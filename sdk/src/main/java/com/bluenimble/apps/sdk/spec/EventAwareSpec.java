package com.bluenimble.apps.sdk.spec;

import java.io.Serializable;
import java.util.Iterator;

import com.bluenimble.apps.sdk.json.JsonObject;

public interface EventAwareSpec extends Serializable {

	Iterator<String>	events();
	JsonObject			event(String id);
	
}
