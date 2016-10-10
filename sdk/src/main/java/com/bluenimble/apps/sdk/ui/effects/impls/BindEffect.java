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
import com.bluenimble.apps.sdk.ui.utils.BindingHelper;

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
			BindingHelper.bindPage (tag, activity, application, page, dh, useDh);
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
				BindingHelper.bindComponent (tag, activity, application, layer, component, dh, useDh);
			} else {
				BindingHelper.bindLayer (tag, application, layer, activity.layer (layer.id ()), dh, useDh);
			}
			
		}
		
	}

}
