package com.bluenimble.apps.sdk.ui.components.impls.list;

import com.bluenimble.apps.sdk.Json;
import com.bluenimble.apps.sdk.Lang;
import com.bluenimble.apps.sdk.application.UIActivity;
import com.bluenimble.apps.sdk.controller.DataHolder;
import com.bluenimble.apps.sdk.json.JsonArray;
import com.bluenimble.apps.sdk.json.JsonObject;
import com.bluenimble.apps.sdk.spec.ApplicationSpec;
import com.bluenimble.apps.sdk.spec.BindingSpec;
import com.bluenimble.apps.sdk.spec.ComponentSpec;
import com.bluenimble.apps.sdk.spec.LayerSpec;
import com.bluenimble.apps.sdk.ui.components.AbstractComponentFactory;
import com.bluenimble.apps.sdk.ui.components.impls.dropdown.DefaultDropDownAdapter;
import com.bluenimble.apps.sdk.ui.components.impls.listeners.EventListener;
import com.bluenimble.apps.sdk.ui.components.impls.listeners.OnMapDragListenerImpl;
import com.bluenimble.apps.sdk.ui.components.impls.listeners.OnMapLongPressListenerImpl;
import com.bluenimble.apps.sdk.ui.components.impls.listeners.OnMapPressListenerImpl;
import com.bluenimble.apps.sdk.ui.components.impls.listeners.OnMarkerDragListenerImpl;
import com.bluenimble.apps.sdk.ui.components.impls.listeners.OnMarkerPressListenerImpl;
import com.bluenimble.apps.sdk.ui.components.impls.listeners.OnRadioSelectedListenerImpl;
import com.bluenimble.apps.sdk.utils.SpecHelper;
import com.google.android.gms.vision.text.Line;

import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;

import java.util.Set;

public class ListFactory extends AbstractComponentFactory {

	private static final long serialVersionUID = 8437367345924192857L;
	
	private static final String Id = "list";

	private static final String DefaultRecordNs = "record";

	interface Custom {
		String Template 	= "template";
		String Namespace 	= "recordNs";
		String Layout 		= "layout";
		String Columns 		= "cols";
		String MultiSelect 	= "multiSelect";
	}

	interface Layouts {
		String Linear 		= "linear";
		String Grid 		= "grid";
	}

	public ListFactory () {
		supportEvent (EventListener.Event.swipe);
		supportEvent (EventListener.Event.scroll);
	}

	@Override
	public String id () {
		return Id;
	}
	
	@Override
	public View create (UIActivity activity, ViewGroup group, LayerSpec layer, ComponentSpec spec, DataHolder dh) {
		RecyclerView list = new RecyclerView (activity);

		String recordNs = (String)spec.get (Custom.Namespace);
		if (Lang.isNullOrEmpty (recordNs)) {
			recordNs = DefaultRecordNs;
		}

		// set the adapter

		String layout = (String)spec.get (Custom.Layout);
		if (Lang.isNullOrEmpty (layout)) {
			layout = Layouts.Linear;
		}

		RecyclerView.LayoutManager layoutManager = null;

		if (layout.equals (Layouts.Linear)) {
			layoutManager = new LinearLayoutManager (activity);
		} else if (layout.equals (Layouts.Grid)) {
			int columns = 2;
			Object oColumns = spec.get (Custom.Columns);
			if (oColumns != null) {
				try {
					columns = Integer.valueOf (String.valueOf (oColumns));
				} catch (Exception ex) {
					// ignore default to 2 columns
				}
			}
			layoutManager = new GridLayoutManager (activity, columns);
		}

		list.setLayoutManager (layoutManager);

		// set the item animator
		// should review
		list.setItemAnimator (new DefaultItemAnimator ());

		// set the adapter
		list.setAdapter (new DefaultListAdapter (activity, spec, recordNs, SpecHelper.getBoolean (spec, Custom.MultiSelect, false)));

		return applyStyle (group, list, spec, dh);
	}

	@Override
	public void bind (ComponentSpec.Binding binding, View view, ApplicationSpec applicationSpec, ComponentSpec spec, DataHolder dh) {
		
		if (view == null || !(view instanceof ListView)) {
			// TODO: log
			return;
		}
		
		BindingSpec bindingSpec = spec.binding (binding);
		if (bindingSpec == null) {
			return;
		}
		
		String [] property = bindingSpec.property ();
		
		RecyclerView list = (RecyclerView)view;

		DefaultListAdapter adapter = (DefaultListAdapter)list.getAdapter ();

		switch (binding) {
			case Set:
				// adapter shouldn't be created in here, because the bind method could be called through an event to populate data
				// load data in Adapter

				if (dh == null) {
					adapter.load (null);
					adapter.notifyDataSetChanged ();
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
				
				// reload data
				boolean hasData = adapter.getRecords () != null;

				// load data in adapter
				adapter.load ((JsonArray)value);

				// notify the view with data change
				if (hasData) {
					adapter.notifyDataSetChanged ();
				}

				break;
			case Get:
				// get the selected, bind the record
				Set<Integer> selected = adapter.selected ();
				if (selected == null || selected.isEmpty ()) {
					return;
				}
				if (SpecHelper.getBoolean (spec, Custom.MultiSelect, false)) {
					for (Integer position : selected) {
						Json.set ((JsonObject)dh.get (bindingSpec.source ()), adapter.getRecords ().get (position), property);
						return;
					}
				}
				JsonArray array = new JsonArray ();
				for (Integer position : selected) {
					array.add (adapter.getRecords ().get (position));
				}
				Json.set ((JsonObject)dh.get (bindingSpec.source ()), array, property);
				break;
			default:
				break;
		}
		
	}

	@Override
	public void addEvent (UIActivity activity, View view, ApplicationSpec applicationSpec, ComponentSpec component, String eventName, JsonObject eventSpec) {
		if (view == null || !(view instanceof RecyclerView)) {
			// TODO: log
			return;
		}
		
		if (!isEventSupported (eventName)) {
			// TODO: log
			return;
		}

		RecyclerView list = (RecyclerView)view;

		EventListener.Event event = EventListener.Event.valueOf (eventName);
		switch (event) {
			case swipe:
				break;
			case scroll:
				break;
			default:
				break;
		}

	}
	
}
