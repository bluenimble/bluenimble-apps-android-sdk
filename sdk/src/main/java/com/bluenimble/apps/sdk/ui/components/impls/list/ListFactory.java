package com.bluenimble.apps.sdk.ui.components.impls.list;

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
import com.bluenimble.apps.sdk.ui.components.impls.listeners.OnRadioSelectedListenerImpl;

import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RadioGroup;

public class ListFactory extends AbstractComponentFactory {

	private static final long serialVersionUID = 8437367345924192857L;
	
	private static final String Id = "list";

	private static final String DefaultRecordNs = "record";

	interface Custom {
		String Template 	= "template";
		String Namespace 	= "recordNs";
	}

	public ListFactory () {
		supportEvent (EventListener.Event.press);
		supportEvent (EventListener.Event.longPress);
		supportEvent (EventListener.Event.swipe);
	}

	@Override
	public String id () {
		return Id;
	}
	
	@Override
	public View create (UIActivity activity, ViewGroup group, LayerSpec layer, ComponentSpec spec) {
		RecyclerView list = new RecyclerView (activity);

		String recordNs = (String)spec.get (Custom.Namespace);
		if (Lang.isNullOrEmpty (recordNs)) {
			recordNs = DefaultRecordNs;
		}

		// set the adapter
		// should review
		RecyclerView.LayoutManager layoutManager = new LinearLayoutManager (activity);
		list.setLayoutManager (layoutManager);

		// set the item animator
		// should review
		list.setItemAnimator (new DefaultItemAnimator ());

		// set the adapter
		list.setAdapter (new DefaultListAdapter (activity, spec, recordNs));

		return applyStyle (group, list, spec);
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
		
		switch (binding) {
			case Set:
				// adapter shouldn't be created in here, because the bind method could be called through an event to populate data
				// load data in Adapter
				DefaultListAdapter adapter = (DefaultListAdapter)list.getAdapter ();

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

		// register press, longpress and swipe

		// list.setOnCheckedChangeListener (new OnRadioSelectedListenerImpl (EventListener.Event.select, eventSpec));

	}
	
}
