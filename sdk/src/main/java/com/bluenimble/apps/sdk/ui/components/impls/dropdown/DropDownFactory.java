package com.bluenimble.apps.sdk.ui.components.impls.dropdown;

import com.bluenimble.apps.sdk.Json;
import com.bluenimble.apps.sdk.application.UIActivity;
import com.bluenimble.apps.sdk.controller.DataHolder;
import com.bluenimble.apps.sdk.json.JsonArray;
import com.bluenimble.apps.sdk.json.JsonObject;
import com.bluenimble.apps.sdk.spec.ApplicationSpec;
import com.bluenimble.apps.sdk.spec.BindingSpec;
import com.bluenimble.apps.sdk.spec.ComponentSpec;
import com.bluenimble.apps.sdk.spec.LayerSpec;
import com.bluenimble.apps.sdk.ui.components.AbstractComponentFactory;
import com.bluenimble.apps.sdk.ui.components.impls.listeners.EventListener;
import com.bluenimble.apps.sdk.ui.components.impls.listeners.OnItemSelectedListenerImpl;

import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.Spinner;

public class DropDownFactory extends AbstractComponentFactory {

	private static final long serialVersionUID = 8437367345924192857L;
	
	private static final String Id = "dropdown";
	
	public interface Custom {
		String Template = "template";
	}

	interface Record {
		String Id 		= "id";
		String Value 	= "value";
		String Current 	= "current";
	}

	public DropDownFactory () {
		supportEvent (EventListener.Event.check);
	}

	@Override
	public String id () {
		return Id;
	}
	
	@Override
	public View create (UIActivity activity, ViewGroup group, LayerSpec layer, ComponentSpec spec) {
		return applyStyle (group, new Spinner (activity), spec);
	}

	@Override
	public void bind (ComponentSpec.Binding binding, View view, ApplicationSpec applicationSpec, ComponentSpec spec, DataHolder dh) {
		
		if (view == null || !(view instanceof RadioGroup)) {
			// TODO: log
			return;
		}
		
		BindingSpec bindingSpec = spec.binding (binding);
		if (bindingSpec == null) {
			return;
		}
		
		String [] property = bindingSpec.property ();
		
		Spinner dropDown = (Spinner)view;
		
		switch (binding) {
			case Set:
				Object value = dh.valueOf (applicationSpec, bindingSpec);
				if (value == null) {
					return;
				}
				
				if (!(value instanceof JsonArray)) {
					// TODO: log
					return;
				}
				
				JsonArray array = (JsonArray)value;
				if (array.count () < 1) {
					// TODO: log
					return;
				}
				
				DefaultDropDownAdapter adapter = new DefaultDropDownAdapter (view.getContext (), applicationSpec, spec, (JsonArray)value);
				dropDown.setAdapter (adapter);
							
				break;
			case Get:
				Json.set ((JsonObject)dh.get (bindingSpec.source ()), dropDown.getSelectedItem (), property);
				break;
			default:
				break;
		}
		
	}

	@Override
	public void addEvent (UIActivity activity, View view, ApplicationSpec applicationSpec, ComponentSpec component, String eventName, JsonObject eventSpec) {
		if (view == null || !(view instanceof Spinner)) {
			// TODO: log
			return;
		}
		
		if (!isEventSupported (eventName)) {
			// TODO: log
			return;
		}
		
		Spinner dropDown = (Spinner)view;
		
		dropDown.setOnItemSelectedListener (new OnItemSelectedListenerImpl (EventListener.Event.check, eventSpec));
	}
}