package com.bluenimble.apps.sdk.ui.components.impls.listeners;

import com.bluenimble.apps.sdk.application.UIActivity;
import com.bluenimble.apps.sdk.controller.ActionProcessor;
import com.bluenimble.apps.sdk.json.JsonArray;
import com.bluenimble.apps.sdk.json.JsonObject;
import com.bluenimble.apps.sdk.ui.components.impls.map.MapFactory;
import com.bluenimble.apps.sdk.ui.components.impls.map.MapFragment;
import com.bluenimble.apps.sdk.ui.components.impls.map.MapTouchableWrapper;
import com.google.android.gms.maps.model.CameraPosition;

/**
 * Created by Mehdi Bendriss on 11/10/2016.
 */

public class OnMapDragListenerImpl extends EventListener implements MapTouchableWrapper.UserInteraction {

	private MapFragment mapFragment;

	public OnMapDragListenerImpl (MapFragment mapFragment, Event event, JsonObject eventSpec) {
		super (event, eventSpec);
		this.mapFragment = mapFragment;
	}

	@Override
	public void onMapUpdated () {
		CameraPosition cameraPosition = mapFragment.getMap ().getCameraPosition ();
		if (cameraPosition != null) {
			JsonObject state 	= new JsonObject ();
			JsonArray loc 		= new JsonArray ();

			loc.add (cameraPosition.target.latitude);
			loc.add (cameraPosition.target.longitude);
			state.set (MapFactory.Record.Loc, loc);

			mapFragment.setState (state);
		}

		ActionProcessor.process (event.name (), eventSpec, (UIActivity)mapFragment.getActivity (), mapFragment.getView (), null);
	}
}