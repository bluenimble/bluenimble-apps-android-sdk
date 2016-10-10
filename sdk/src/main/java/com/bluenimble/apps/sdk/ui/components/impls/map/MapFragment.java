package com.bluenimble.apps.sdk.ui.components.impls.map;

import com.bluenimble.apps.sdk.Lang;
import com.bluenimble.apps.sdk.controller.DataHolder;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class MapFragment extends SupportMapFragment {

	private View 				view;
	private MapTouchableWrapper touchable;
	
	private GoogleMap  map;
	private DataHolder data;

	private String component;

	public static MapFragment create (String component) {
		MapFragment f = new MapFragment ();
		f.component 	= component;
		return f;
	}

	@Override
	public View onCreateView (LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
		view = super.onCreateView (inflater, parent, savedInstanceState);
		touchable = new MapTouchableWrapper (getActivity ());
		touchable.addView (view);
		
		// set the id of the component as the tag of the view "layerId.componentId"
		if (component != null) {
			view.setTag (component);
		}
		
		return touchable;
	}

	@Override
	public View getView () {
		return view;
	}

	public GoogleMap getMap () {
		return map;
	}

	public void setMap (GoogleMap map) {
		this.map = map;
	}

	public DataHolder getData () {
		return data;
	}

	public void setData (DataHolder data) {
		this.data = data;
	}

}