package com.bluenimble.apps.sdk.controller;

import android.view.View;

import com.bluenimble.apps.sdk.application.UIActivity;
import com.bluenimble.apps.sdk.json.JsonObject;

import java.io.Serializable;

public interface Controller extends Serializable {

	Action 	lockup		(String id);
	void 	register	(Action action);

	void 	process 	(ActionInstance actionInstance, UIActivity activity, boolean checkPermissions);
	
}
