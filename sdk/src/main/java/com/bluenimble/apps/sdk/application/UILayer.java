package com.bluenimble.apps.sdk.application;

import com.bluenimble.apps.sdk.Json;
import com.bluenimble.apps.sdk.Lang;
import com.bluenimble.apps.sdk.controller.DataHolder;
import com.bluenimble.apps.sdk.json.JsonObject;
import com.bluenimble.apps.sdk.spec.ApplicationSpec;
import com.bluenimble.apps.sdk.spec.LayerSpec;
import com.bluenimble.apps.sdk.spec.StyleSpec;
import com.bluenimble.apps.sdk.ui.components.impls.listeners.EventListener;
import com.bluenimble.apps.sdk.ui.themes.impls.JsonStyleSpec;
import com.bluenimble.apps.sdk.utils.SpecHelper;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import java.util.HashSet;
import java.util.Set;

public class UILayer extends Fragment {
		
/*
	public interface Exchange {
		String Event 	= "event";
		String Data 	= "data";
	}
*/

	protected 	LayerSpec 	layer;
	protected 	DataHolder 	dh;

	public UILayer () {
	}

	public static UILayer create (LayerSpec layer, DataHolder dh) {

		UILayer fragment = new UILayer ();

		fragment.setRetainInstance (true);

		fragment.layer 	= layer;
		fragment.dh 	= dh; // dh == null ? null : dh.clone ();

		return fragment;

	}
	
	@Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle state) {
		UIActivity 		activity 	= (UIActivity)getActivity ();
		ApplicationSpec application = ((UIApplication)activity.getApplication ()).getSpec ();
		return application.renderer ().render (application, layer, dh, container, activity);
    }
	
	@Override
    public void onViewCreated (View container, Bundle state) {
		UIActivity activity = (UIActivity)getActivity ();

		// if follow
		View vFollow = null;
		String follow = (String)layer.style ().get (StyleSpec.Follow);
		if (!Lang.isNullOrEmpty (follow)) {
			vFollow = activity.findView (follow);
		}
		if (vFollow != null) {
			int sibling = vFollow.getId ();
			Log.d (JsonStyleSpec.class.getSimpleName (), "\t-> addRule BELOW " + Lang.ARRAY_OPEN + follow + Lang.SLASH + sibling + Lang.ARRAY_CLOSE);
			FrameLayout group = (FrameLayout)container.getParent ();
			((RelativeLayout.LayoutParams)group.getLayoutParams ()).addRule (RelativeLayout.BELOW, sibling);
		}

		SpecHelper.fireCreateEvent (layer, layer.id (), (UIActivity)getActivity (), activity.root (), true, dh);
    }
	
}
