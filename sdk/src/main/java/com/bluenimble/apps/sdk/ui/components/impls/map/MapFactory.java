package com.bluenimble.apps.sdk.ui.components.impls.map;

import com.bluenimble.apps.sdk.Json;
import com.bluenimble.apps.sdk.application.UIActivity;
import com.bluenimble.apps.sdk.controller.DataHolder;
import com.bluenimble.apps.sdk.json.JsonArray;
import com.bluenimble.apps.sdk.json.JsonObject;
import com.bluenimble.apps.sdk.spec.ApplicationSpec;
import com.bluenimble.apps.sdk.spec.BindingSpec;
import com.bluenimble.apps.sdk.spec.ComponentSpec;
import com.bluenimble.apps.sdk.spec.LayerSpec;
import com.bluenimble.apps.sdk.ui.components.AbstractComponentFactory;
import com.bluenimble.apps.sdk.ui.components.impls.listeners.EventListener;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

public class MapFactory extends AbstractComponentFactory {

	private static final long serialVersionUID = 8437367345924192857L;
	
	private static final String Id = "map";

	interface Record {
		String Id 		= "id";
		String Lat 		= "lat";
		String Lng 		= "lng";
		String Name 	= "name";
	}

	public MapFactory () {
		supportEvent (EventListener.Event.move);
		supportEvent (EventListener.Event.markerPress);
	}

	@Override
	public String id () {
		return Id;
	}
	
	@Override
	public View create (UIActivity activity, ViewGroup group, LayerSpec layer, ComponentSpec spec) {
		
		MapFragment fragment = new MapFragment (layer.id (), spec.id ());
		
		FragmentManager manager = activity.getSupportFragmentManager ();
		FragmentTransaction transaction = manager.beginTransaction ();
		transaction.add (group.getId (), fragment, spec.id ());
		
		transaction.commit ();
		
		return null;
	}

	@Override
	public void bind (ComponentSpec.Binding binding, View view, ApplicationSpec applicationSpec, ComponentSpec spec, DataHolder dh) {
		Log.d (MapFactory.class.getSimpleName (), " .bind > view: " + String.valueOf (view));
	
		if (view == null) {
			// TODO: log
			return;
		}
		
		FragmentManager manager = ((UIActivity)view.getContext ()).getSupportFragmentManager ();
		
		MapFragment mapFragment = (MapFragment)manager.findFragmentByTag (spec.id ());
		
		GoogleMap map = mapFragment.getMap ();
		
		BindingSpec bindingSpec = spec.binding (binding);
		if (bindingSpec == null) {
			return;
		}
		
		String [] property = bindingSpec.property ();
		
		switch (binding) {
			case Set:
				Object value = dh.valueOf (applicationSpec, bindingSpec);
				if (value == null) {
					return;
				}
				
				if (!(value instanceof JsonArray)) {
					// TODO: log
					return;
				}
				
				JsonArray array = (JsonArray)value;
				if (array.isEmpty ()) {
					return;
				}
				
				LatLngBounds.Builder builder = new LatLngBounds.Builder ();
				
				for (int i = 0; i < array.count (); i++) {
					JsonObject record = (JsonObject)array.get (i);
					MarkerOptions markerOptions = new MarkerOptions ()
                            .position (new LatLng (Json.getDouble (record, Record.Lat, 0), Json.getDouble (record, Record.Lng, 0)))
                            .title (Json.getString (record, Record.Name));
                            //.icon (BitmapDescriptorFactory.fromResource (0)); // need to review based on "custom": { "icon": "house_icon": { "name": "houseType", "value": "Home" } }
					Marker marker = map.addMarker (markerOptions);
                    marker.setTitle (Json.getString (record, Record.Id));
                    builder.include (marker.getPosition ());
				}
				
				// relocate camera
                map.animateCamera (CameraUpdateFactory.newLatLngBounds (builder.build (), 0));
                
				break;
			case Get:
				Json.set ((JsonObject)dh.get (bindingSpec.source ()), view.getTag (), property);
				break;
			default:
				break;
		}
	
	}

	@Override
	public void addEvent (UIActivity activity, View view, ApplicationSpec applicationSpec, ComponentSpec component, String eventName, JsonObject eventSpec) {
		FragmentManager manager = activity.getSupportFragmentManager ();
		
		MapFragment mapFragment = (MapFragment)manager.findFragmentByTag (component.id ());
		
		GoogleMap map = mapFragment.getMap ();
		
		map.setOnMapLoadedCallback ();
		
	}

}
