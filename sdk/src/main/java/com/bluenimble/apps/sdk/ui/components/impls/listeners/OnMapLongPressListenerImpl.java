package com.bluenimble.apps.sdk.ui.components.impls.listeners;

import com.bluenimble.apps.sdk.application.UIActivity;
import com.bluenimble.apps.sdk.controller.ActionProcessor;
import com.bluenimble.apps.sdk.controller.DataHolder;
import com.bluenimble.apps.sdk.controller.impls.DefaultDataHolder;
import com.bluenimble.apps.sdk.json.JsonObject;
import com.bluenimble.apps.sdk.ui.components.impls.map.MapFactory;
import com.bluenimble.apps.sdk.ui.components.impls.map.MapFragment;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Mehdi Bendriss on 11/10/2016.
 */

public class OnMapLongPressListenerImpl extends EventListener implements GoogleMap.OnMapLongClickListener {

	private MapFragment mapFragment;

	public OnMapLongPressListenerImpl (MapFragment mapFragment, Event event, JsonObject eventSpec) {
		super (event, eventSpec);
		this.mapFragment = mapFragment;
	}

	@Override
	public void onMapLongClick (LatLng latLng) {
		DataHolder dh = null;
		if (latLng != null) {
			JsonObject loc 	= new JsonObject ();
			loc.set (MapFactory.Record.Lat, latLng.latitude);
			loc.set (MapFactory.Record.Lng, latLng.longitude);

			dh 	= new DefaultDataHolder ();
			dh.set (MapFactory.Record.Loc, loc);
		}

		ActionProcessor.process (event.name (), eventSpec, (UIActivity)mapFragment.getActivity (), mapFragment.getView (), dh);
	}
}