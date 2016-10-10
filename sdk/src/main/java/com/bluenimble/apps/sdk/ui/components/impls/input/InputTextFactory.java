package com.bluenimble.apps.sdk.ui.components.impls.input;

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
import com.bluenimble.apps.sdk.ui.components.impls.listeners.OnChangeListenerImpl;

import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

public class InputTextFactory extends AbstractComponentFactory {

	private static final long serialVersionUID = 8437367345924192857L;
	
	private static final String Id = "input";

	interface Custom {
		String Hint 	= "hint";
	}

	public InputTextFactory () {
		supportEvent (OnChangeListenerImpl.Event.onTextChanged); 
		supportEvent (OnChangeListenerImpl.Event.beforeTextChanged); 
		supportEvent (OnChangeListenerImpl.Event.afterTextChanged);
	}

	@Override
	public String id () {
		return Id;
	}
	
	@Override
	public View create (UIActivity activity, ViewGroup group, LayerSpec layer, ComponentSpec spec) {
		EditText input = new EditText (activity);
		
		return applyStyle (group, input, spec);
	}

	@Override
	public void bind (ComponentSpec.Binding binding, View view, ApplicationSpec application, ComponentSpec spec, DataHolder dh) {
		
		if (view == null || !(view instanceof EditText)) {
			// TODO: log
			application.logger ().debug ("Bind InputTextFactory", "view == null || !(view instanceof EditText)");
			return;
		}
		
		// bind the hit
		if (binding.equals (ComponentSpec.Binding.Set)) {
			String hint = (String)spec.get (Custom.Hint);
			application.logger ().debug (InputTextFactory.class.getSimpleName (), "Hint " + hint + Lang.ARRAY_CLOSE);
			
			if (!Lang.isNullOrEmpty (hint)) {
				Object vHint = application.i18nProvider ().get (Lang.split (hint, Lang.DOT), dh);
				if (vHint == null) {
					vHint = hint;
				}
				((EditText)view).setHint (String.valueOf (vHint));
			}
		}
		
		BindingSpec bindingSpec = spec.binding (binding);
		if (bindingSpec == null) {
			// TODO: log
			application.logger ().debug ("Bind InputTextFactory", "bindingSpec == null");
			return;
		}
		
		String [] property = bindingSpec.property ();
		
		switch (binding) {
			case Set:
				if (dh == null) {
					((EditText)view).setText (null);
					return;
				}
				Object value = dh.valueOf (application, bindingSpec);
				application.logger ().debug (InputTextFactory.class.getSimpleName (), "Binding." + binding + Lang.ARRAY_OPEN + spec.id () + Lang.SPACE + view.getId () + Lang.ARRAY_CLOSE + Lang.EQUALS + value);
				if (value == null) {
					return;
				}
				((EditText)view).setText (value.toString ());
				break;
			case Get:
				application.logger ().debug (InputTextFactory.class.getSimpleName (), "Binding." + binding + Lang.ARRAY_OPEN + spec.id () + Lang.SPACE + view.getId () + Lang.ARRAY_CLOSE + Lang.EQUALS + ((EditText)view).getText ().toString ());
				application.logger ().debug (InputTextFactory.class.getSimpleName (), "\tSource=" + bindingSpec.source () + "\tProperty=" + property);
				Json.set ((JsonObject)dh.get (bindingSpec.source ()), ((EditText)view).getText ().toString (), property);
				break;
			default:
				break;
		}
		
	}

	@Override
	public void addEvent (UIActivity activity, View view, ApplicationSpec applicationSpec, ComponentSpec component, String eventName, JsonObject eventSpec) {
		if (view == null || !(view instanceof EditText)) {
			// TODO: log
			return;
		}
		
		if (!isEventSupported (eventName)) {
			// TODO: log
			return;
		}
		
		EditText text = (EditText)view;
		
		text.addTextChangedListener (new OnChangeListenerImpl (text, EventListener.Event.valueOf (eventName), eventSpec));
		
	}

}
