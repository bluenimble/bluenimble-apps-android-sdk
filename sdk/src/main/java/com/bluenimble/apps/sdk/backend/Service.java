package com.bluenimble.apps.sdk.backend;

import android.content.Context;

import java.io.Serializable;

import com.bluenimble.apps.sdk.application.UIActivity;
import com.bluenimble.apps.sdk.controller.DataHolder;
import com.bluenimble.apps.sdk.json.JsonObject;
import com.bluenimble.apps.sdk.spec.ApplicationSpec;

public interface Service extends Serializable {
	
	interface Type {
		String Local 	= "prefs";
		String Remote 	= "remote";
		String Storage 	= "storage";
	}

	// TODO : find a better way to get the context instead of passing UIActivity
	void execute	(String id, JsonObject spec, ApplicationSpec application, DataHolder dh, Context appContext) throws Exception;
	
}
