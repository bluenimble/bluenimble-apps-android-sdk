package com.bluenimble.apps.sdk.ui.components;

import java.io.Serializable;

import com.bluenimble.apps.sdk.application.UIActivity;
import com.bluenimble.apps.sdk.controller.DataHolder;
import com.bluenimble.apps.sdk.json.JsonObject;
import com.bluenimble.apps.sdk.spec.ApplicationSpec;
import com.bluenimble.apps.sdk.spec.ComponentSpec;
import com.bluenimble.apps.sdk.spec.LayerSpec;

import android.view.View;
import android.view.ViewGroup;

public interface ComponentFactory extends Serializable {

	interface Custom {
		String Constant = "constant";
	}

	String	id 			();
	
	View 	create		(UIActivity activity, ViewGroup group, LayerSpec layer, ComponentSpec component, DataHolder dh);
	
	void 	bind		(ComponentSpec.Binding binding, View view, ApplicationSpec applicationSpec, ComponentSpec spec, DataHolder dh);
	
	void	addEvent	(UIActivity activity, View view, ApplicationSpec applicationSpec, ComponentSpec component, String eventName, JsonObject eventSpec);

	boolean	isAutoBind	();

	void	destroy		(UIActivity activity, View view, ApplicationSpec applicationSpec, ComponentSpec component);

}
