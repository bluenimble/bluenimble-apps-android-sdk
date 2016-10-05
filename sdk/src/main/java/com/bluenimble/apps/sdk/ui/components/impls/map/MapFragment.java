package com.bluenimble.apps.sdk.ui.components.impls.map;

import com.bluenimble.apps.sdk.Lang;
import com.google.android.gms.maps.SupportMapFragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class MapFragment extends SupportMapFragment {

	private View 				view;
	private MapTouchableWrapper touchable;
	
	private String layer;
	private String component;

	public MapFragment (String layer, String component) {
		this.layer 		= layer;
		this.component 	= component;
	}
	
	@Override
	public View onCreateView (LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
		view = super.onCreateView (inflater, parent, savedInstanceState);
		touchable = new MapTouchableWrapper (getActivity ());
		touchable.addView (view);
		
		// set the id of the component as the tag of the view "layerId.componentId"
		if (component != null) {
			view.setTag (layer + Lang.DOT + component);
		}
		
		return touchable;
	}

	@Override
	public View getView () {
		return view;
	}
}