package com.bluenimble.apps.sdk.backend;

import java.io.Serializable;

import com.bluenimble.apps.sdk.controller.DataHolder;
import com.bluenimble.apps.sdk.json.JsonObject;

public interface Service extends Serializable {
	
	interface Type {
		String Local 	= "local";
		String Remote 	= "remote";
		String Storage 	= "storage";
	}
	
	void execute(String id, JsonObject spec, DataHolder dh) throws Exception;
	
}
