package com.bluenimble.apps.sdk.controller;

import java.io.Serializable;

import com.bluenimble.apps.sdk.Json;
import com.bluenimble.apps.sdk.Lang;
import com.bluenimble.apps.sdk.Spec;
import com.bluenimble.apps.sdk.application.UIActivity;
import com.bluenimble.apps.sdk.json.JsonObject;

import android.view.View;

public class ActionProcessor implements Serializable {

	private static final long serialVersionUID = 6926039907807796916L;
	
	public static void process (String eventName, JsonObject eventSpec, UIActivity activity, View view, DataHolder dh) {

		activity.getSpec ().logger ().debug (ActionProcessor.class.getSimpleName (), "Process Event [" + eventName + "] with\n" + eventSpec.toString (2));
		
		Action action = null; 
		
		String actionId = Json.getString (eventSpec, Spec.page.event.Action);
		
		Controller controller = activity.getSpec ().controller ();
		
		if (controller != null && !Lang.isNullOrEmpty (actionId)) {
			action = controller.lockup (actionId);
		}
		if (action == null) {
			action = Action.Default;
		}
		
		action.execute (eventSpec, view, activity, dh);
	}
	
}
