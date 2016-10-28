package com.bluenimble.apps.sdk.controller;

import java.io.Serializable;

import com.bluenimble.apps.sdk.application.UIActivity;
import com.bluenimble.apps.sdk.controller.impls.actions.DefaultAction;
import com.bluenimble.apps.sdk.json.JsonObject;

import android.view.View;

public interface Action extends Serializable {

	interface Scope {
		String Page = "page";
		String None = "none";
	}

	interface Device {
		String Geo 			= "geo";
		String Latitude 	= "lat";
		String Longitude 	= "long";
		String Ids 			= "ids";
		String Provider = "provider";
	}

	Action Default = new DefaultAction ();
	
	String	id		();
	void 	execute (ActionInstance actionInstance, UIActivity activity);
	
}
