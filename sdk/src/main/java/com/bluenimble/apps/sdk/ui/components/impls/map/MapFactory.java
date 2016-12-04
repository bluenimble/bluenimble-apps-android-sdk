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
import com.bluenimble.apps.sdk.ui.components.impls.listeners.OnMapLongPressListenerImpl;
import com.bluenimble.apps.sdk.ui.components.impls.listeners.OnMapDragListenerImpl;
import com.bluenimble.apps.sdk.ui.components.impls.listeners.OnMapPressListenerImpl;
import com.bluenimble.apps.sdk.ui.components.impls.listeners.OnMarkerDragListenerImpl;
import com.bluenimble.apps.sdk.ui.components.impls.listeners.OnMarkerPressListenerImpl;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import java.util.Iterator;

public class MapFactory extends AbstractComponentFactory {

	private static final long serialVersionUID = 8437367345924192857L;
	
	private static final String Id = "map";

	public interface Record {
		String Id 		= "id";
		String Loc 		= "loc";
		String Name 	= "name";
	}

	public MapFactory () {
		supportEvent (EventListener.Event.move);
		supportEvent (EventListener.Event.press);
		supportEvent (EventListener.Event.longPress);
		supportEvent (EventListener.Event.markerDrag);
		supportEvent (EventListener.Event.markerStartDrag);
		supportEvent (EventListener.Event.markerEndDrag);
		supportEvent (EventListener.Event.markerPress);
	}

	@Override
	public String id () {
		return Id;
	}
	
	@Override
	public View create (final UIActivity activity, ViewGroup group, LayerSpec layer, final ComponentSpec spec, DataHolder dh) {
		
		final MapFragment fragment = MapFragment.create (spec);
		
		FragmentManager manager = activity.getSupportFragmentManager ();
		FragmentTransaction transaction = manager.beginTransaction ();

		transaction.add (group.getId (), fragment, spec.id ());
		
		transaction.commit ();

		fragment.getMapAsync (new OnMapReadyCallback () {
			@Override
			public void onMapReady (final GoogleMap map) {
				fragment.setMap (map);

				// bind data
				bind (ComponentSpec.Binding.Set, fragment.getView (), activity.getSpec (), spec, fragment.getData ());

				// set events
				Iterator<String> events = spec.events ();
				if (events == null) {
					return;
				}
				while (events.hasNext ()) {
					String eventId = events.next ();
					addEvent (activity, fragment.getView (), activity.getSpec (), spec, eventId, spec.event (eventId));
				}
			}
		});

		return null;
	}

	@Override
	public void bind (ComponentSpec.Binding binding, View view, ApplicationSpec applicationSpec, ComponentSpec spec, DataHolder dh) {
		Log.d (MapFactory.class.getSimpleName (), " .bind > view: " + String.valueOf (view));
	
		FragmentManager manager = ((UIActivity)view.getContext ()).getSupportFragmentManager ();
		
		MapFragment mapFragment = (MapFragment)manager.findFragmentByTag (spec.id ());
		
		GoogleMap map = mapFragment.getMap ();

		if (map == null) {
			mapFragment.setData (dh);
			// TODO: log
			return;
		}

		BindingSpec bindingSpec = spec.binding (binding);
		if (bindingSpec == null) {
			return;
		}
		
		String [] property = bindingSpec.property ();
		
		switch (binding) {
			case Set:
				if (dh == null) {
					map.clear ();
					return;
				}
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
					JsonArray loc = Json.getArray (record, Record.Loc);
					if (loc == null || loc.isEmpty ()) {
						continue;
					}

					double lat, lng;
					try {
						lat = Double.valueOf ((String)loc.get (0));
					} catch (Exception ex) {
						continue;
					}
					try {
						lng = Double.valueOf ((String)loc.get (1));
					} catch (Exception ex) {
						continue;
					}

					MarkerOptions markerOptions = new MarkerOptions ()
                            .position (new LatLng (lat, lng))
                            .title (Json.getString (record, Record.Name));
                            //.icon (BitmapDescriptorFactory.fromResource (0)); // need to review based on "custom": { "icon": "house_icon": { "name": "houseType", "value": "Home" } }
					Marker marker = map.addMarker (markerOptions);
                    marker.setTag (Json.getString (record, Record.Id));

                    builder.include (marker.getPosition ());
				}
				
				// relocate camera
                map.animateCamera (CameraUpdateFactory.newLatLngBounds (builder.build (), 0));
                
				break;
			case Get:
				Json.set ((JsonObject)dh.get (bindingSpec.source ()), mapFragment.getState (), property);
				break;
			default:
				break;
		}
	
	}

	@Override
	public void addEvent (final UIActivity activity, View view, final ApplicationSpec applicationSpec, final ComponentSpec component, String eventName, JsonObject eventSpec) {
		FragmentManager manager = activity.getSupportFragmentManager ();
		final MapFragment mapFragment = (MapFragment)manager.findFragmentByTag (component.id ());

		GoogleMap map = mapFragment.getMap ();

		if (map == null) {
			// TODO: log
			return;
		}

		if (!isEventSupported (eventName)) {
			// TODO: log
			return;
		}

		EventListener.Event event = EventListener.Event.valueOf (eventName);
		switch (event) {
			case press:
				map.setOnMapClickListener (new OnMapPressListenerImpl (mapFragment, event, eventSpec));
				break;
			case longPress:
				map.setOnMapLongClickListener (new OnMapLongPressListenerImpl (mapFragment, event, eventSpec));
				break;
			case move:
				mapFragment.getTouchableView ().setOnMapUpdated (new OnMapDragListenerImpl (mapFragment, event, eventSpec));
				break;
			case markerStartDrag:
				map.setOnMarkerDragListener (new OnMarkerDragListenerImpl (mapFragment, event, eventSpec));
				break;
			case markerDrag:
				map.setOnMarkerDragListener (new OnMarkerDragListenerImpl (mapFragment, event, eventSpec));
				break;
			case markerEndDrag:
				map.setOnMarkerDragListener (new OnMarkerDragListenerImpl (mapFragment, event, eventSpec));
				break;
			case markerPress:
				map.setOnMarkerClickListener (new OnMarkerPressListenerImpl (mapFragment, event, eventSpec));
				break;
			default:
				break;
		}
	}
	@Override
	public void destroy (UIActivity activity, View view, ApplicationSpec applicationSpec, ComponentSpec component) {
		FragmentManager manager = ((UIActivity)view.getContext ()).getSupportFragmentManager ();

		MapFragment mapFragment = (MapFragment)manager.findFragmentByTag (component.id ());
		if (mapFragment == null) {
			return;
		}

		FragmentTransaction transaction = manager.beginTransaction ();

		// remove fragment
		transaction.remove (mapFragment);

		// commit
		transaction.commit ();

	}

}