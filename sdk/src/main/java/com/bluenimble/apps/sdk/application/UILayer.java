package com.bluenimble.apps.sdk.application;

import com.bluenimble.apps.sdk.Spec;
import com.bluenimble.apps.sdk.controller.ActionProcessor;
import com.bluenimble.apps.sdk.controller.DataHolder;
import com.bluenimble.apps.sdk.json.JsonObject;
import com.bluenimble.apps.sdk.spec.ApplicationSpec;
import com.bluenimble.apps.sdk.spec.LayerSpec;
import com.bluenimble.apps.sdk.ui.effects.impls.BindEffect;
import com.bluenimble.apps.sdk.ui.renderer.impls.DefaultRenderer.LifeCycleEvent;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class UILayer extends Fragment {
		
	public interface Exchange {
		String Event 	= "event";
		String Data 	= "data";
		String Layer 	= "layer";
	}
	
	private 	JsonObject 	createEvent;
	private 	DataHolder 	dh;
	
	protected 	LayerSpec 	layer;

	public UILayer () {
	}

	public static UILayer create (LayerSpec layer, DataHolder dh) {

		UILayer fragment = new UILayer ();

		fragment.setRetainInstance (true);

		fragment.layer 	= layer;
		fragment.dh 	= dh;
		fragment.createEvent = (JsonObject)new JsonObject ()
				.set (Spec.Action.OnStart, new JsonObject ()
						.set (BindEffect.Id, layer.id ()));

		return fragment;

	}
	
	@Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle state) {
		UIActivity activity = (UIActivity)getActivity ();
		
		ApplicationSpec application = ((UIApplication)activity.getApplication ()).getSpec ();
		
		return application.renderer ().render (application, layer, null, container, activity);
    }
	
	@Override
    public void onViewCreated (View container, Bundle state) {
		
		UIActivity activity = (UIActivity)getActivity ();
		ApplicationSpec application = ((UIApplication)activity.getApplication ()).getSpec ();
		
		// run default page create event / bind all
		ActionProcessor.process (LifeCycleEvent.create.name (), createEvent, activity, activity.root (), dh);

		// run page create event if any
		JsonObject eventSpec = application.renderer ().current ().event (LifeCycleEvent.create.name ());
		if (eventSpec != null) {
			ActionProcessor.process (LifeCycleEvent.create.name (), eventSpec, activity, activity.root (), dh);
		}
    }
	
}
