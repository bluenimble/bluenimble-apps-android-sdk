package com.bluenimble.apps.sdk.ui.components.impls.listeners;

import com.bluenimble.apps.sdk.application.UIActivity;
import com.bluenimble.apps.sdk.controller.ActionProcessor;
import com.bluenimble.apps.sdk.json.JsonArray;
import com.bluenimble.apps.sdk.json.JsonObject;
import com.bluenimble.apps.sdk.ui.components.impls.map.MapFactory;
import com.bluenimble.apps.sdk.ui.components.impls.map.MapFragment;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

/**
 * Created by Mehdi Bendriss on 11/10/2016.
 */

public class OnMarkerDragListenerImpl extends EventListener implements GoogleMap.OnMarkerDragListener {

	private MapFragment mapFragment;

	public OnMarkerDragListenerImpl (MapFragment mapFragment, Event event, JsonObject eventSpec) {
		super (event, eventSpec);
		this.mapFragment = mapFragment;
	}

	@Override
	public void onMarkerDragStart (Marker marker) {
		if (!Event.markerStartDrag.equals (event)) {
			return;
		}
		mapFragment.setState (getState (marker));
		ActionProcessor.process (event.name (), eventSpec, (UIActivity)mapFragment.getActivity (), mapFragment.getView (), null);
	}

	@Override
	public void onMarkerDrag (Marker marker) {
		if (!Event.markerDrag.equals (event)) {
			return;
		}
		mapFragment.setState (getState (marker));
		ActionProcessor.process (event.name (), eventSpec, (UIActivity)mapFragment.getActivity (), mapFragment.getView (), null);
	}

	@Override
	public void onMarkerDragEnd (Marker marker) {
		if (!Event.markerEndDrag.equals (event)) {
			return;
		}
		mapFragment.setState (getState (marker));
		ActionProcessor.process (event.name (), eventSpec, (UIActivity)mapFragment.getActivity (), mapFragment.getView (), null);
	}

	private JsonObject getState (Marker marker) {
		JsonObject state 	= new JsonObject ();
		JsonArray loc 		= new JsonArray ();

		loc.add (marker.getPosition ().latitude);
		loc.add (marker.getPosition ().longitude);
		state.set (MapFactory.Record.Loc, loc);
		state.set (MapFactory.Record.Id, marker.getId ());

		return state;
	}
}