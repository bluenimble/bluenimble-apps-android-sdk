package com.bluenimble.apps.sdk.ui.effects.impls;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.bluenimble.apps.sdk.Lang;
import com.bluenimble.apps.sdk.application.UIActivity;
import com.bluenimble.apps.sdk.controller.DataHolder;
import com.bluenimble.apps.sdk.controller.impls.AgnosticDataHolder;
import com.bluenimble.apps.sdk.spec.ApplicationSpec;
import com.bluenimble.apps.sdk.spec.ComponentSpec;
import com.bluenimble.apps.sdk.spec.ComponentSpec.Binding;
import com.bluenimble.apps.sdk.spec.LayerSpec;
import com.bluenimble.apps.sdk.spec.PageSpec;
import com.bluenimble.apps.sdk.ui.effects.Effect;

import android.util.Log;
import android.view.View;

public class BindEffect implements Effect {

	private static final long serialVersionUID = 3783743185246914342L;
	
	public static final String Id = "bind";

	private boolean useDh = true;
	
	public BindEffect () {
	}
	
	public BindEffect (boolean useDh) {
		this.useDh = useDh;
	}
	
	@Override
	public String id () {
		return Id;
	}

	@Override
	public void apply (UIActivity activity, ApplicationSpec application, PageSpec page, Object spec, DataHolder dh) {
		
		if (spec == null || !(spec instanceof String)) {
			return;
		}
		
		String tag = this.getClass ().getSimpleName ();

		Log.d (tag, "\t-> Process Effect [" + getClass ().getSimpleName () + "] Spec => " + spec);

		String list = (String)spec;
		
		if (Lang.isNullOrEmpty (list)) {
			return;
		}
		
		String [] uis = Lang.split (list, Lang.COMMA, true);
		Set<String> sUis = new HashSet<String>(Arrays.asList (uis));
		
		if (sUis.contains (Lang.STAR)) {
			bindPage (tag, activity, application, page, dh);
			return;
		}
		
		for (String ui : uis) {
			Log.d (tag, "\t-> Bind [" + ui + "] ");
			if (ui.equals (Lang.STAR)) {
				continue;
			}
			String componentId = null; 
			String layerId = ui; 
			int indexOfDot = ui.indexOf (Lang.DOT);
			if (indexOfDot > 0) {
				layerId = ui.substring (0, indexOfDot);
				componentId = ui.substring (indexOfDot + 1);
			}
			Log.d (tag, "\t-> Bind [" + layerId + " . " + componentId + "] ");
			
			LayerSpec layer = page.layer (layerId);
			if (layer == null) {
				// TODO log
				Log.d (tag, "\t-> Layer [" + layerId + "] not found");
				continue;
			}
			ComponentSpec component = null;
			if (!Lang.isNullOrEmpty (componentId)) {
				component = layer.component (componentId);
				if (component == null) {
					// TODO log
					Log.d (tag, "\t-> Component [" + componentId + "] not found");
					continue;
				}
			}
			
			if (component != null) {
				bindComponent (tag, activity, application, layer, component, dh);
			} else {
				bindLayer (tag, activity, application, layer, dh);
			}
			
		}
		
	}
	
	private void bindComponent (String tag, UIActivity activity, ApplicationSpec application, LayerSpec layer, ComponentSpec component, DataHolder dh) {
		Log.d (tag, "\t\t    -> Bind Component [" + component.type () + "/" + component.id () + "]");
		View view = activity.component (layer.id (), component.id ());
		if (view == null) {
			Log.d (BindEffect.class.getSimpleName (), "\t\t    -> ERR: View Not found [" + layer.id () + Lang.DOT + component.id () + "]");
			return;
		}
		application.componentsRegistry ().lookup (component.type ()).bind (Binding.Set, view, application, component, useDh ? dh : AgnosticDataHolder.Instance);
	}

	private void bindLayer (String tag, UIActivity activity, ApplicationSpec application, LayerSpec layer, DataHolder dh) {
		if (activity.layer (layer.id ()) == null || layer.count () == 0) {
			return;
		}
		
		Log.d (tag, "\t\t  -> Bind Layer [" + layer.id () + "]");

		for (int i = 0; i < layer.count (); i++) {
			bindComponent (tag, activity, application, layer, layer.component (i), dh);
		}
	}
	
	private void bindPage (String tag, UIActivity activity, ApplicationSpec application, PageSpec page, DataHolder dh) {
		if (page.count () == 0) {
			return;
		}
		
		Log.d (tag, "\t\t-> Bind Page ...");

		Iterator<String> layers = page.layers ();
		while (layers.hasNext ()) {
			bindLayer (tag, activity, application, page.layer (layers.next ()), dh);
		}
	}

}
