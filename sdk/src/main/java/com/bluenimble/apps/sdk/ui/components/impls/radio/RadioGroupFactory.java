package com.bluenimble.apps.sdk.ui.components.impls.radio;

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
import com.bluenimble.apps.sdk.ui.components.impls.listeners.OnRadioSelectedListenerImpl;

import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class RadioGroupFactory extends AbstractComponentFactory {

	private static final long serialVersionUID = 8437367345924192857L;
	
	private static final String Id = "radioGroup";

	interface Record {
		String Id 		= "id";
		String Value 	= "value";
		String Current 	= "current";
	}

	public RadioGroupFactory () {
		supportEvent (EventListener.Event.select);
	}

	@Override
	public String id () {
		return Id;
	}
	
	@Override
	public View create (UIActivity activity, ViewGroup group, LayerSpec layer, ComponentSpec spec) {
		return applyStyle (group, new RadioGroup (activity), spec);
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
		
		RadioGroup group = (RadioGroup)view;
		
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
				if (array.count () < 2) {
					// TODO: log
					return;
				}
				
				for (int i = 0; i < array.count (); i++) {
					Object record = array.get (i);
					RadioButton radio = new RadioButton (view.getContext ());
					if (record instanceof JsonObject) {
						JsonObject oRecord = (JsonObject)record;
						radio.setText 	(Json.getString (oRecord, Record.Value));
						radio.setTag 	(Json.getString (oRecord, Record.Id));
						radio.setChecked (Json.getBoolean (oRecord, Record.Current, false));
					} else {
						String radioValue = String.valueOf (record);
						radio.setText 	(radioValue);
						radio.setTag 	(String.valueOf (i));
					}
					group.addView (radio);
				}
							
				break;
			case Get:
				for (int i = 0; i < group.getChildCount (); i++) {
					RadioButton btn = (RadioButton)group.getChildAt (i);
					if (btn.isChecked ()) {
						Json.set ((JsonObject)dh.get (bindingSpec.source ()), btn.getTag (), property);
						break;
					}
				}
				break;
			default:
				break;
		}
		
	}

	@Override
	public void addEvent (UIActivity activity, View view, ApplicationSpec applicationSpec, ComponentSpec component, String eventName, JsonObject eventSpec) {
		if (view == null || !(view instanceof RadioGroup)) {
			// TODO: log
			return;
		}
		
		if (!isEventSupported (eventName)) {
			// TODO: log
			return;
		}
		
		RadioGroup radioGroup = (RadioGroup)view;
		
		radioGroup.setOnCheckedChangeListener (new OnRadioSelectedListenerImpl (EventListener.Event.select, eventSpec));
		
	}
	
}
