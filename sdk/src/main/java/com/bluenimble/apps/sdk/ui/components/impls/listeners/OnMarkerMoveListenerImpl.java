package com.bluenimble.apps.sdk.ui.components.impls.listeners;

import com.bluenimble.apps.sdk.application.UIActivity;
import com.bluenimble.apps.sdk.controller.ActionProcessor;
import com.bluenimble.apps.sdk.controller.DataHolder;
import com.bluenimble.apps.sdk.controller.impls.DefaultDataHolder;
import com.bluenimble.apps.sdk.json.JsonObject;
import com.bluenimble.apps.sdk.ui.components.impls.map.MapFactory;
import com.bluenimble.apps.sdk.ui.components.impls.map.MapFragment;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

/**
 * Created by Mehdi Bendriss on 11/10/2016.
 */

public class OnMarkerMoveListenerImpl extends EventListener implements GoogleMap.OnMarkerDragListener {

	private MapFragment mapFragment;

	public OnMarkerMoveListenerImpl (MapFragment mapFragment, Event event, JsonObject eventSpec) {
		super (event, eventSpec);
		this.mapFragment = mapFragment;
	}

	@Override
	public void onMarkerDragStart (Marker marker) {
		DataHolder dh = getDh (marker, "dragStart");
		ActionProcessor.process (event.name (), eventSpec, (UIActivity)mapFragment.getActivity (), mapFragment.getView (), dh);
	}

	@Override
	public void onMarkerDrag (Marker marker) {
		DataHolder dh = getDh (marker, "drag");
		ActionProcessor.process (event.name (), eventSpec, (UIActivity)mapFragment.getActivity (), mapFragment.getView (), dh);
	}

	@Override
	public void onMarkerDragEnd (Marker marker) {
		DataHolder dh = getDh (marker, "dragEnd");
		ActionProcessor.process (event.name (), eventSpec, (UIActivity)mapFragment.getActivity (), mapFragment.getView (), dh);
	}

	private DataHolder getDh (Marker marker, String status) {
		JsonObject oDh 	= new JsonObject ();
		JsonObject loc 	= new JsonObject ();

		loc.set (MapFactory.Record.Lat, marker.getPosition ().latitude);
		loc.set (MapFactory.Record.Lng, marker.getPosition ().longitude);

		oDh.set (MapFactory.Record.Loc, loc);
		oDh.set (MapFactory.Record.Status, status);

		DataHolder dh 	= new DefaultDataHolder ();
		dh.set (MapFactory.Record.Marker, oDh);

		return dh;
	}
}