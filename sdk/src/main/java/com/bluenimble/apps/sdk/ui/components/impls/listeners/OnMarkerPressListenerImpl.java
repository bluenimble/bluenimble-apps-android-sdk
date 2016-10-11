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

public class OnMarkerPressListenerImpl extends EventListener implements GoogleMap.OnMarkerClickListener {

	private MapFragment mapFragment;

	public OnMarkerPressListenerImpl (MapFragment mapFragment, Event event, JsonObject eventSpec) {
		super (event, eventSpec);
		this.mapFragment = mapFragment;
	}

	@Override
	public boolean onMarkerClick (Marker marker) {
		JsonObject state 	= new JsonObject ();
		JsonArray loc 		= new JsonArray ();

		loc.add (marker.getPosition ().latitude);
		loc.add (marker.getPosition ().longitude);
		state.set (MapFactory.Record.Loc, loc);
		state.set (MapFactory.Record.Id, marker.getId ());

		mapFragment.setState (state);
		ActionProcessor.process (event.name (), eventSpec, (UIActivity)mapFragment.getActivity (), mapFragment.getView (), null);

		return true;
	}
}