package com.bluenimble.apps.sdk.backend;

import java.io.Serializable;

import com.bluenimble.apps.sdk.controller.DataHolder;
import com.bluenimble.apps.sdk.json.JsonObject;

public interface DataVisitor extends Serializable {
	
	String		id();

	JsonObject 	onRequest(DataHolder dh);
	JsonObject 	onResponse(DataHolder dh);
	
}
