package com.bluenimble.apps.sdk.ui.components.impls.text;

import com.bluenimble.apps.sdk.Json;
import com.bluenimble.apps.sdk.Lang;
import com.bluenimble.apps.sdk.application.UIActivity;
import com.bluenimble.apps.sdk.controller.DataHolder;
import com.bluenimble.apps.sdk.json.JsonObject;
import com.bluenimble.apps.sdk.spec.ApplicationSpec;
import com.bluenimble.apps.sdk.spec.BindingSpec;
import com.bluenimble.apps.sdk.spec.ComponentSpec;
import com.bluenimble.apps.sdk.spec.LayerSpec;
import com.bluenimble.apps.sdk.spec.impls.json.JsonEventAwareSpec;
import com.bluenimble.apps.sdk.ui.components.AbstractComponentFactory;
import com.bluenimble.apps.sdk.ui.components.impls.listeners.EventListener;
import com.bluenimble.apps.sdk.ui.components.impls.listeners.OnLongPressListenerImpl;
import com.bluenimble.apps.sdk.ui.components.impls.listeners.OnPressListenerImpl;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class TextFactory extends AbstractComponentFactory {

	private static final long serialVersionUID = 8437367345924192857L;
	
	private static final String DefaultText = "Label";

	public static final String Id = "text";

	public TextFactory () {
		supportEvent (EventListener.Event.press);
		supportEvent (EventListener.Event.longPress);
	}

	@Override
	public String id () {
		return Id;
	}
	
	@Override
	public View create (UIActivity activity, ViewGroup group, LayerSpec layer, ComponentSpec spec, DataHolder dh) {
		return applyStyle (group, new TextView (activity), spec, dh);
	}

	@Override
	public void bind (ComponentSpec.Binding binding, View view, ApplicationSpec application, ComponentSpec spec, DataHolder dh) {
		
		if (view == null || !(view instanceof TextView)) {
			// TODO: log
			return;
		}
		
		TextView text = (TextView)view;
		
		BindingSpec bindingSpec = spec.binding (binding);
		if (bindingSpec == null) {
			if (ComponentSpec.Binding.Set.equals (binding)) {
				text.setText (DefaultText);
			}
			return;
		}
		
		String [] property = bindingSpec.property ();
		
		switch (binding) {
			case Set:
				if (dh == null) {
					text.setText (null);
					return;
				}
				Object value = null;
				if (spec.get (Custom.Constant) != null && (boolean)spec.get (Custom.Constant)) {
					value = Lang.join (bindingSpec.property (), Lang.DOT);
				} else {
					value = dh.valueOf (application, bindingSpec);
					application.logger ().debug (TextFactory.class.getSimpleName (), "Binding." + binding + Lang.ARRAY_OPEN + spec.id () + Lang.SPACE + view.getId () + Lang.ARRAY_CLOSE + Lang.EQUALS + value);
				}
				if (value == null) {
					return;
				}
				text.setText (value.toString ());
				break;
			case Get:
				Json.set ((JsonObject)dh.get (bindingSpec.source ()), text.getText ().toString (), property);
				break;
			default:
				break;
		}
		
	}

	@Override
	public void addEvent (UIActivity activity, View view, ApplicationSpec applicationSpec, ComponentSpec component, String eventName, JsonObject eventSpec) {
		if (view == null || !(view instanceof TextView)) {
			// TODO: log
			return;
		}

		Log.d (JsonEventAwareSpec.class.getSimpleName (), "Attach Event : " + eventName);
		
		if (!isEventSupported (eventName)) {
			// TODO: log
			return;
		}

		Log.d (JsonEventAwareSpec.class.getSimpleName (), "\tsetClickable");

		TextView text = (TextView)view;
		
		if (!text.isClickable ()) {
			text.setClickable (true);
		}
		
		EventListener.Event event = EventListener.Event.valueOf (eventName);
		switch (event) {
			case press:
				text.setOnClickListener (new OnPressListenerImpl (event, eventSpec));
				break;
			case longPress:
				text.setOnLongClickListener (new OnLongPressListenerImpl (event, eventSpec));
				break;
			default:
				break;
		}
		
	}

}
