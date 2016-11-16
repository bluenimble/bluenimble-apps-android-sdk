package com.bluenimble.apps.sdk.ui.components.impls.listeners;

import com.bluenimble.apps.sdk.application.UIActivity;
import com.bluenimble.apps.sdk.controller.impls.actions.DefaultActionInstance;
import com.bluenimble.apps.sdk.json.JsonArray;
import com.bluenimble.apps.sdk.json.JsonObject;
import com.bluenimble.apps.sdk.spec.ApplicationSpec;
import com.bluenimble.apps.sdk.ui.components.impls.map.MapFactory;
import com.bluenimble.apps.sdk.ui.components.impls.map.MapFragment;
import com.bluenimble.apps.sdk.ui.components.impls.map.MapTouchableWrapper;
import com.bluenimble.apps.sdk.utils.SpecHelper;
import com.google.android.gms.maps.model.CameraPosition;

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
		ApplicationSpec application = SpecHelper.application (mapFragment.getView ());
		application.controller ()
			.process (
				DefaultActionInstance.create (event.name (), eventSpec, application, null, mapFragment.getView ()),
				(UIActivity)mapFragment.getActivity (),
				true
			);

	}
}