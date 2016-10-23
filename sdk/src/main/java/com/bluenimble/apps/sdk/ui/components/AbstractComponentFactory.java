package com.bluenimble.apps.sdk.ui.components;

import java.util.HashSet;
import java.util.Set;

import com.bluenimble.apps.sdk.Lang;
import com.bluenimble.apps.sdk.application.UIActivity;
import com.bluenimble.apps.sdk.application.UIApplication;
import com.bluenimble.apps.sdk.controller.DataHolder;
import com.bluenimble.apps.sdk.json.JsonObject;
import com.bluenimble.apps.sdk.spec.ApplicationSpec;
import com.bluenimble.apps.sdk.spec.ComponentSpec;
import com.bluenimble.apps.sdk.ui.components.impls.listeners.EventListener;

import android.view.View;
import android.view.ViewGroup;

public abstract class AbstractComponentFactory implements ComponentFactory {

	private static final long serialVersionUID = 4847839513064104897L;
	
	protected Set<String> supportedEvents;
	
	protected AbstractComponentFactory () {
	}

	@Override
	public void bind (ComponentSpec.Binding binding, View view, ApplicationSpec applicationSpec, ComponentSpec spec, DataHolder dh) {
	}
	
	@Override
	public void addEvent (UIActivity activity, View view, ApplicationSpec applicationSpec, ComponentSpec component, String eventName, JsonObject eventSpec) {
	}
	
	protected View applyStyle (ViewGroup group, View component, ComponentSpec spec, DataHolder dh) {
		if (spec.style () == null) {
			return component;
		}
		
		component.setId (UIApplication.newId ());
		
		spec.style ().apply (spec, component, group, dh);
		
		return component;
	}
	
	protected boolean isEventSupported (String name) {
		if (supportedEvents == null || supportedEvents.isEmpty ()) {
			return false;
		}
		if (Lang.isNullOrEmpty (name)) {
			return false;
		}
		return supportedEvents.contains (name);
	}
	
	protected void supportEvent (EventListener.Event event) {
		if (event == null) {
			return;
		}
		if (supportedEvents == null) {
			supportedEvents = new HashSet<String> ();
		}
		supportedEvents.add (event.name ());
	}
	
}
