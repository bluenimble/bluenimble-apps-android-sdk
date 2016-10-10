package com.bluenimble.apps.sdk.ui.components.impls.checkbox;

import com.bluenimble.apps.sdk.Json;
import com.bluenimble.apps.sdk.Lang;
import com.bluenimble.apps.sdk.application.UIActivity;
import com.bluenimble.apps.sdk.controller.DataHolder;
import com.bluenimble.apps.sdk.json.JsonObject;
import com.bluenimble.apps.sdk.spec.ApplicationSpec;
import com.bluenimble.apps.sdk.spec.BindingSpec;
import com.bluenimble.apps.sdk.spec.ComponentSpec;
import com.bluenimble.apps.sdk.spec.LayerSpec;
import com.bluenimble.apps.sdk.ui.components.AbstractComponentFactory;
import com.bluenimble.apps.sdk.ui.components.impls.listeners.EventListener;
import com.bluenimble.apps.sdk.ui.components.impls.listeners.OnCheckedListenerImpl;

import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

public class CheckboxFactory extends AbstractComponentFactory {

	private static final long serialVersionUID = 8437367345924192857L;
	
	private static final String Id = "checkbox";
	
	public CheckboxFactory () {
		supportEvent (EventListener.Event.select);
	}

	@Override
	public String id () {
		return Id;
	}
	
	@Override
	public View create (UIActivity activity, ViewGroup group, LayerSpec layer, ComponentSpec spec) {
		return applyStyle (group, new CheckBox (activity), spec);
	}

	@Override
	public void bind (ComponentSpec.Binding binding, View view, ApplicationSpec applicationSpec, ComponentSpec spec, DataHolder dh) {
		
		if (view == null || !(view instanceof CheckBox)) {
			// TODO: log
			return;
		}
		
		BindingSpec bindingSpec = spec.binding (binding);
		if (bindingSpec == null) {
			return;
		}
		
		String [] property = bindingSpec.property ();
		
		switch (binding) {
			case Set:
				if (dh == null) {
					((CheckBox)view).setChecked (false);
					return;
				}
				Object value = dh.valueOf (applicationSpec, bindingSpec);
				if (value == null) {
					return;
				}
				String sValue = String.valueOf (value);
				boolean itsTrue = Lang.TrueValues.contains (sValue);
				if (itsTrue) {
					((CheckBox)view).setChecked (true);
				} else {
					boolean itsFalse = Lang.FalseValues.contains (sValue);
					if (itsFalse) {
						((CheckBox)view).setChecked (false);
					} 
				}				
				break;
			case Get:
				Json.set ((JsonObject)dh.get (bindingSpec.source ()), ((CheckBox)view).isChecked (), property);
				break;
			default:
				break;
		}
		
	}

	@Override
	public void addEvent (UIActivity activity, View view, ApplicationSpec applicationSpec, ComponentSpec component, String eventName, JsonObject eventSpec) {
		if (view == null || !(view instanceof CheckBox)) {
			// TODO: log
			return;
		}
		
		if (!isEventSupported (eventName)) {
			// TODO: log
			return;
		}
		
		CheckBox checkbox = (CheckBox)view;
		
		checkbox.setOnCheckedChangeListener (new OnCheckedListenerImpl (EventListener.Event.valueOf (eventName), eventSpec));
		
	}

}
