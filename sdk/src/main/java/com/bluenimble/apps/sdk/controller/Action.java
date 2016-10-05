package com.bluenimble.apps.sdk.controller;

import java.io.Serializable;

import com.bluenimble.apps.sdk.application.UIActivity;
import com.bluenimble.apps.sdk.controller.impls.DefaultAction;
import com.bluenimble.apps.sdk.json.JsonObject;

import android.view.View;

public interface Action extends Serializable {
	
	Action Default = new DefaultAction ();
	
	String	id();
	void 	execute(JsonObject spec, View view, UIActivity activity, DataHolder dh);
	
}
