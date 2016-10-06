package com.bluenimble.apps.sdk.ui.effects.impls;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import com.bluenimble.apps.sdk.Lang;
import com.bluenimble.apps.sdk.application.UIActivity;
import com.bluenimble.apps.sdk.application.UILayer;
import com.bluenimble.apps.sdk.controller.ActionProcessor;
import com.bluenimble.apps.sdk.controller.DataHolder;
import com.bluenimble.apps.sdk.json.JsonObject;
import com.bluenimble.apps.sdk.spec.ApplicationSpec;
import com.bluenimble.apps.sdk.spec.LayerSpec;
import com.bluenimble.apps.sdk.spec.PageSpec;
import com.bluenimble.apps.sdk.ui.effects.Effect;
import com.bluenimble.apps.sdk.ui.renderer.impls.DefaultRenderer.LifeCycleEvent;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

public class RenderEffect implements Effect {

	private static final long serialVersionUID = 3783743185246914342L;
	
	private static final String Id = "render";

	@Override
	public String id () {
		return Id;
	}

	@Override
	public void apply (UIActivity activity, ApplicationSpec application, PageSpec page, Object spec, DataHolder dh) {
		
		if (spec == null || !(spec instanceof String)) {
			return;
		}

		String list = (String)spec;
		
		if (Lang.isNullOrEmpty (list)) {
			return;
		}
		
		String [] aList = Lang.split (list, Lang.COMMA, true);
		
		Set<String> pageOrLayers = new HashSet<String>(Arrays.asList (aList));
		
		if (pageOrLayers.contains (Lang.STAR)) {
			application.renderer ().render (page, activity);
			return;
		}
		
		FragmentManager manager = activity.getSupportFragmentManager ();
		FragmentTransaction transaction = manager.beginTransaction ();

		for (String layerId : pageOrLayers) {
			LayerSpec layer = page.layer (layerId);
			if (layer == null) {
				// TODO log
				continue;
			}
			Fragment f = manager.findFragmentByTag (layer.id ());
			if (f != null) {
				JsonObject eventSpec = layer.event (LifeCycleEvent.destroy.name ());
				if (eventSpec != null) {
					ActionProcessor.process (LifeCycleEvent.destroy.name (), eventSpec, (UIActivity)f.getView ().getContext (), f.getView (), dh);
				}

				transaction.remove (f);
			}
			
			Fragment newFragment = UILayer.create (layer, dh);
			// add the 
			transaction.add (activity.root ().getId (), newFragment, layer.id ());
		}
		
		transaction.commit ();
		
	}

}
