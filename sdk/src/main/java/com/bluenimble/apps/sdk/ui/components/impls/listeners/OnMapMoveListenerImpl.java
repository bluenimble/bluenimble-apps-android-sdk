package com.bluenimble.apps.sdk.ui.components.impls.listeners;

import com.bluenimble.apps.sdk.application.UIActivity;
import com.bluenimble.apps.sdk.controller.ActionProcessor;
import com.bluenimble.apps.sdk.controller.DataHolder;
import com.bluenimble.apps.sdk.controller.impls.DefaultDataHolder;
import com.bluenimble.apps.sdk.json.JsonObject;
import com.bluenimble.apps.sdk.ui.components.impls.map.MapFactory;
import com.bluenimble.apps.sdk.ui.components.impls.map.MapFragment;
import com.bluenimble.apps.sdk.ui.components.impls.map.MapTouchableWrapper;
import com.google.android.gms.maps.model.CameraPosition;

/**
 * Created by Mehdi Bendriss on 11/10/2016.
 */

public class OnMapMoveListenerImpl extends EventListener implements MapTouchableWrapper.UserInteraction {

	private MapFragment mapFragment;

	public OnMapMoveListenerImpl (MapFragment mapFragment, Event event, JsonObject eventSpec) {
		super (event, eventSpec);
		this.mapFragment = mapFragment;
	}

	@Override
	public void onMapUpdated () {
		DataHolder dh = null;

		CameraPosition cameraPosition = mapFragment.getMap ().getCameraPosition ();
		if (cameraPosition != null) {
			JsonObject loc 	= new JsonObject ();
			loc.set (MapFactory.Record.Lat, cameraPosition.target.latitude);
			loc.set (MapFactory.Record.Lng, cameraPosition.target.longitude);

			dh 	= new DefaultDataHolder ();
			dh.set (MapFactory.Record.Loc, loc);
		}

		ActionProcessor.process (event.name (), eventSpec, (UIActivity)mapFragment.getActivity (), mapFragment.getView (), dh);
	}
}