package com.bluenimble.apps.sdk.ui.effects.impls;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.bluenimble.apps.sdk.Json;
import com.bluenimble.apps.sdk.Lang;
import com.bluenimble.apps.sdk.application.UIActivity;
import com.bluenimble.apps.sdk.application.ux.LayerLayout;
import com.bluenimble.apps.sdk.controller.DataHolder;
import com.bluenimble.apps.sdk.json.JsonObject;
import com.bluenimble.apps.sdk.spec.ApplicationSpec;
import com.bluenimble.apps.sdk.spec.ComponentSpec;
import com.bluenimble.apps.sdk.spec.ComponentSpec.Binding;
import com.bluenimble.apps.sdk.spec.LayerSpec;
import com.bluenimble.apps.sdk.spec.PageSpec;
import com.bluenimble.apps.sdk.ui.effects.Effect;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import android.util.Log;
import android.view.View;

public class AnimateEffect implements Effect {

	private static final long serialVersionUID = 3783743185246914342L;
	
	private static final String Id = "animate";

	interface Spec {
		String Effect 	= "effect";
		String Time		= "time";
	}

	private static final Map<String, Techniques> VisualEffects = new HashMap<String, Techniques> ();
	static {
		for (Techniques t : Techniques.values ()) {
			VisualEffects.put (t.name ().toLowerCase (), t);
		}
	}

	@Override
	public String id () {
		return Id;
	}
	
	@Override
	public void apply (UIActivity activity, ApplicationSpec application, PageSpec page, Object spec, View origin, DataHolder dh) {
		
		if (spec == null) {
			return;
		}
		
		Log.d (AnimateEffect.class.getSimpleName (), "\t-> Process Effect [" + getClass ().getSimpleName () + "] Spec => " + spec);

		JsonObject list = (JsonObject)spec;
		
		if (Json.isNullOrEmpty (list)) {
			return;
		}

		Iterator<String> keys = list.keys ();

		while (keys.hasNext ()) {
			String ui = keys.next ();
			Log.d (AnimateEffect.class.getSimpleName (), "\t-> Bind [" + ui + "] ");

			String componentId = null;
			String layerId = ui; 
			int indexOfDot = ui.indexOf (Lang.DOT);
			if (indexOfDot > 0) {
				layerId = ui.substring (0, indexOfDot);
				componentId = ui.substring (indexOfDot + 1);
			}
			Log.d (AnimateEffect.class.getSimpleName (), "\t-> Bind [" + layerId + " . " + componentId + "] ");
			
			LayerSpec layer = page.layer (layerId);
			if (layer == null) {
				// TODO log
				Log.d (AnimateEffect.class.getSimpleName (), "\t-> Layer [" + layerId + "] not found");
				continue;
			}
			ComponentSpec component = null;
			if (!Lang.isNullOrEmpty (componentId)) {
				component = layer.component (componentId);
				if (component == null) {
					// TODO log
					Log.d (AnimateEffect.class.getSimpleName (), "\t-> Component [" + componentId + "] not found");
					continue;
				}
			}

			View layerView = activity.findView (layerId);
			if (layerView == null || !(layerView instanceof LayerLayout)) {
				continue;
			}

			View view = null;

			if (component != null) {
				view = ((LayerLayout)layerView).findView (component.id ());
			} else {
				view = layerView;
			}
			if (view == null) {
				Log.d (AnimateEffect.class.getSimpleName (), "\t\t    -> ERR: View Not found [" + layer.id () + Lang.DOT + componentId + "]");
				continue;
			}

			String 	vEffect 	= Techniques.Flash.name ();
			long 	time 		= 500;

			Object o = list.get (ui);
			if (o instanceof String) {
				vEffect = (String)o;
			} else if (o instanceof JsonObject) {
				JsonObject oEffect = (JsonObject)o;
				vEffect = Json.getString (oEffect, Spec.Effect, vEffect);
				time = Json.getLong (oEffect, Spec.Time, time);
			}

			vEffect = vEffect.toLowerCase ();

			Techniques t = VisualEffects.get (vEffect);
			if (t == null) {
				t = Techniques.Flash;
			}

			// animate view
			YoYo.with (t).duration (time).playOn (view);

		}
		
	}

}
