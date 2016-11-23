package com.bluenimble.apps.sdk.ui.effects.impls;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import com.bluenimble.apps.sdk.Lang;
import com.bluenimble.apps.sdk.application.UIActivity;
import com.bluenimble.apps.sdk.controller.DataHolder;
import com.bluenimble.apps.sdk.controller.impls.actions.DefaultActionInstance;
import com.bluenimble.apps.sdk.json.JsonObject;
import com.bluenimble.apps.sdk.spec.ApplicationSpec;
import com.bluenimble.apps.sdk.spec.LayerSpec;
import com.bluenimble.apps.sdk.spec.PageSpec;
import com.bluenimble.apps.sdk.ui.effects.Effect;
import com.bluenimble.apps.sdk.ui.renderer.Renderer.LifeCycleEvent;

import android.view.View;

public class RenderEffect implements Effect {

	private static final long serialVersionUID = 3783743185246914342L;
	
	private static final String Id = "render";

	@Override
	public String id () {
		return Id;
	}

	@Override
	public void apply (UIActivity activity, ApplicationSpec application, PageSpec page, Object spec, View origin, DataHolder dh) {
		
		if (spec == null || !(spec instanceof String)) {
			return;
		}

		String list = (String)spec;
		
		if (Lang.isNullOrEmpty (list)) {
			return;
		}
		
		String [] aList = Lang.split (list, Lang.SPACE, true);
		
		Set<String> pageOrLayers = new HashSet<String>(Arrays.asList (aList));
		
		if (pageOrLayers.contains (Lang.STAR)) {
			application.renderer ().render (page, activity, dh);
			return;
		}

		for (String layerId : pageOrLayers) {
			LayerSpec layer = page.layer (layerId);
			if (layer == null) {
				// TODO log
				continue;
			}
			View layerView = activity.findView (layer.id ());
			if (layerView != null) {
				JsonObject eventSpec = layer.event (LifeCycleEvent.destroy.name ());
				if (eventSpec != null) {
					application.controller ().process (
						DefaultActionInstance.create (LifeCycleEvent.destroy.name (), eventSpec, application, dh, layerView), activity, true
					);
				}

				activity.root ().removeView (layerView);
			}
			
			// add the
			activity.root ().addView (application.renderer ().render (application, layer, dh, activity.root (), activity));
		}
		
	}

}
