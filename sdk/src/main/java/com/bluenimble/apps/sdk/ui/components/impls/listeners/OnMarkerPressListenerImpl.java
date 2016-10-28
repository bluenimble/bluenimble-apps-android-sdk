package com.bluenimble.apps.sdk.ui.components.impls.listeners;

import com.bluenimble.apps.sdk.application.UIActivity;
import com.bluenimble.apps.sdk.controller.impls.actions.DefaultActionInstance;
import com.bluenimble.apps.sdk.json.JsonArray;
import com.bluenimble.apps.sdk.json.JsonObject;
import com.bluenimble.apps.sdk.ui.components.impls.map.MapFactory;
import com.bluenimble.apps.sdk.ui.components.impls.map.MapFragment;
import com.bluenimble.apps.sdk.utils.SpecHelper;
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

		SpecHelper.application (mapFragment.getView ())
			.controller ()
				.process (
					DefaultActionInstance.create (event.name (), eventSpec, null, mapFragment.getView ()),
					(UIActivity)mapFragment.getView ().getContext (),
					true
				);

		return true;
	}
}