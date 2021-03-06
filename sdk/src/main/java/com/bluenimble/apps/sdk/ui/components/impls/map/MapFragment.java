package com.bluenimble.apps.sdk.ui.components.impls.map;

import com.bluenimble.apps.sdk.controller.DataHolder;
import com.bluenimble.apps.sdk.json.JsonObject;
import com.bluenimble.apps.sdk.spec.ComponentSpec;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Map;

public class MapFragment extends SupportMapFragment {

	private View 				view;
	private MapTouchableWrapper touchable;
	
	private GoogleMap  map;
	private DataHolder data;

	private JsonObject state;

	private ComponentSpec component;

	private Map<String, JsonObject> events = null;

	public static MapFragment create (ComponentSpec component) {
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
			view.setTag (component.id ());
		}
		
		return touchable;
	}

	@Override
	public View getView () {
		return view;
	}

	public MapTouchableWrapper getTouchableView () {
		return touchable;
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

	public JsonObject getState () {
		return state;
	}

	public void setState (JsonObject state) {
		this.state = state;
	}

	public ComponentSpec setComponent () {
		return component;
	}

}