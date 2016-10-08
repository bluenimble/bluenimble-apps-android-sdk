package com.bluenimble.apps.sdk.ui.components.impls.generic;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.bluenimble.apps.sdk.Json;
import com.bluenimble.apps.sdk.Lang;
import com.bluenimble.apps.sdk.application.UIActivity;
import com.bluenimble.apps.sdk.application.UIApplication;
import com.bluenimble.apps.sdk.controller.DataHolder;
import com.bluenimble.apps.sdk.controller.impls.AgnosticDataHolder;
import com.bluenimble.apps.sdk.json.JsonObject;
import com.bluenimble.apps.sdk.spec.ApplicationSpec;
import com.bluenimble.apps.sdk.spec.BindingSpec;
import com.bluenimble.apps.sdk.spec.ComponentSpec;
import com.bluenimble.apps.sdk.spec.LayerSpec;
import com.bluenimble.apps.sdk.ui.components.AbstractComponentFactory;
import com.bluenimble.apps.sdk.ui.components.impls.listeners.EventListener;
import com.bluenimble.apps.sdk.ui.components.impls.listeners.OnLongPressListenerImpl;
import com.bluenimble.apps.sdk.ui.components.impls.listeners.OnPressListenerImpl;
import com.bluenimble.apps.sdk.ui.effects.impls.BindEffect;

public class LayerFactory extends AbstractComponentFactory {

	private static final long serialVersionUID = 8437367345924192857L;
	
	public static final String Id = "layer";
	
	public LayerFactory() {
	}

	@Override
	public String id () {
		return Id;
	}
	
	@Override
	public View create (UIActivity activity, ViewGroup group, LayerSpec layer, ComponentSpec spec) {

		LayerSpec layerToRender = spec.layer ();
		if (layerToRender == null) {
			return null;
		}

		ApplicationSpec application = ((UIApplication)activity.getApplication ()).getSpec ();

		return applyStyle (group, application.renderer ().render (application, layerToRender, null, group, activity), spec);
	}

	@Override
	public void bind (ComponentSpec.Binding binding, View view, ApplicationSpec application, ComponentSpec spec, DataHolder dh) {
		LayerSpec layer = spec.layer ();
		if (layer == null) {
			return;
		}

		UIActivity activity = (UIActivity)view.getContext ();

		for (int i = 0; i < layer.count (); i++) {
			ComponentSpec subComponent = layer.component (i);
			View subView = activity.component (layer.id (), subComponent.id ());
			if (subView == null) {
				Log.d (BindEffect.class.getSimpleName (), "\t\t    -> ERR: View Not found [" + layer.id () + Lang.DOT + subComponent.id () + "]");
				return;
			}
			application.componentsRegistry ().lookup (subComponent.type ()).bind (binding, view, application, subComponent, dh);
		}

	}

}
