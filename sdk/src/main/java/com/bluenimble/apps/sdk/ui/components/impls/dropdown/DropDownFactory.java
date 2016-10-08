package com.bluenimble.apps.sdk.ui.components.impls.dropdown;

import com.bluenimble.apps.sdk.Json;
import com.bluenimble.apps.sdk.Lang;
import com.bluenimble.apps.sdk.application.UIActivity;
import com.bluenimble.apps.sdk.controller.DataHolder;
import com.bluenimble.apps.sdk.json.JsonArray;
import com.bluenimble.apps.sdk.json.JsonObject;
import com.bluenimble.apps.sdk.spec.ApplicationSpec;
import com.bluenimble.apps.sdk.spec.BindingSpec;
import com.bluenimble.apps.sdk.spec.ComponentSpec;
import com.bluenimble.apps.sdk.spec.LayerSpec;
import com.bluenimble.apps.sdk.spec.PageSpec;
import com.bluenimble.apps.sdk.ui.components.AbstractComponentFactory;
import com.bluenimble.apps.sdk.ui.components.impls.listeners.EventListener;
import com.bluenimble.apps.sdk.ui.components.impls.listeners.OnItemSelectedListenerImpl;
import com.bluenimble.apps.sdk.ui.utils.SpecHelper;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.Spinner;

public class DropDownFactory extends AbstractComponentFactory {

	private static final long serialVersionUID = 8437367345924192857L;
	
	private static final String Id = "dropdown";

	private static final String DefaultRecordNs = "record";

	public interface Custom {
		String Template 	= "template";
		String Namespace 	= "recordNs";
	}

	public DropDownFactory () {
		supportEvent (EventListener.Event.select);
	}

	@Override
	public String id () {
		return Id;
	}
	
	@Override
	public View create (UIActivity activity, ViewGroup group, LayerSpec layer, ComponentSpec spec) {

		Spinner dropdown = new Spinner (activity);

		LayerSpec template = SpecHelper.template (activity.getSpec (), (String)spec.get (Custom.Template));

		String recordNs = (String)spec.get (Custom.Namespace);
		if (Lang.isNullOrEmpty (recordNs)) {
			recordNs = DefaultRecordNs;
		}

		DefaultDropDownAdapter adapter = new DefaultDropDownAdapter (activity, spec, template, recordNs);
		dropdown.setAdapter (adapter);

		return applyStyle (group, dropdown, spec);
	}

	@Override
	public void bind (ComponentSpec.Binding binding, View view, ApplicationSpec applicationSpec, ComponentSpec spec, DataHolder dh) {
		
		if (view == null || !(view instanceof Spinner)) {
			// TODO: log
			return;
		}
		
		BindingSpec bindingSpec = spec.binding (binding);
		if (bindingSpec == null) {
			return;
		}
		
		String [] property = bindingSpec.property ();
		
		Spinner dropdown = (Spinner)view;
		
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

				// adapter shouldn't be created in here, because the bind method could be called through an event to populate data
				// load data in Adapter
				DefaultDropDownAdapter adapter = (DefaultDropDownAdapter)dropdown.getAdapter ();
				adapter.load ((JsonArray)value);

				break;
			case Get:
				Json.set ((JsonObject)dh.get (bindingSpec.source ()), dropdown.getSelectedItem (), property);
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
		
		dropDown.setOnItemSelectedListener (new OnItemSelectedListenerImpl (EventListener.Event.select, eventSpec));
	}
}