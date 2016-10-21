package com.bluenimble.apps.sdk.ui.components.impls.listeners;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewParent;

import java.io.Serializable;

import com.bluenimble.apps.sdk.json.JsonObject;
import com.bluenimble.apps.sdk.ui.components.impls.list.DefaultListAdapter;

public class EventListener implements Serializable {

	private static final long serialVersionUID = 6926039907807796916L;

	public static final int Selected 	= 1000;
	public static final int Parent 		= 2000;
	public static final int Type 		= 3000;

	public enum Event {
		onTextChanged,
		beforeTextChanged,
		afterTextChanged,

		select,

		swipe,

		press,
		longPress,

		move,

		scroll,

		markerStartDrag,
		markerDrag,
		markerEndDrag,
		markerPress
	}
	
	protected Event 		event;
	protected JsonObject 	eventSpec;
	
	protected EventListener (Event event, JsonObject eventSpec) {
		this.event 		= event;
		this.eventSpec 	= eventSpec;
	}

	protected void markAsSelected (View view) {
		// check if this is a grid record
		Integer position = (Integer)view.getTag (EventListener.Selected);
		if (position == null || position == RecyclerView.NO_POSITION) {
			return;
		}

		// get the adapter and set selected
		ViewParent parent = view.getParent ();
		if (parent != null && parent instanceof RecyclerView) {
			RecyclerView list = (RecyclerView)parent;
			((DefaultListAdapter)list.getAdapter ()).select (position);
		}
	}
	
}
