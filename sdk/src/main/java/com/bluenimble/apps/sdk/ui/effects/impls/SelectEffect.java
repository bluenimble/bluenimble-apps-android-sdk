package com.bluenimble.apps.sdk.ui.effects.impls;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.bluenimble.apps.sdk.Lang;
import com.bluenimble.apps.sdk.application.UIActivity;
import com.bluenimble.apps.sdk.controller.DataHolder;
import com.bluenimble.apps.sdk.spec.ApplicationSpec;
import com.bluenimble.apps.sdk.spec.PageSpec;
import com.bluenimble.apps.sdk.ui.components.impls.list.DefaultListAdapter;
import com.bluenimble.apps.sdk.ui.components.impls.listeners.EventListener;
import com.bluenimble.apps.sdk.ui.effects.Effect;

public class SelectEffect implements Effect {

	private static final long serialVersionUID = 3783743185246914342L;
	
	private static final String Id = "select";

	@Override
	public String id () {
		return Id;
	}

	@Override
	public void apply (UIActivity activity, ApplicationSpec application, PageSpec page, Object spec, View origin, DataHolder dh) {
		
		if (spec == null || !(spec instanceof String)) {
			return;
		}

		String gridId = (String)spec;

		int indexOfDot = gridId.indexOf (Lang.DOT);
		if (indexOfDot < 0) {
			return;
		}

		View view = activity.component (gridId.substring (0, indexOfDot), gridId.substring (indexOfDot + 1));
		if (view == null || !(view instanceof RecyclerView)) {
			return;
		}

		DefaultListAdapter adapter = (DefaultListAdapter)((RecyclerView)view).getAdapter ();

		// find the view record then the position
		// find parent with tag
		View record = activity.parent (EventListener.Selected, origin);
		if (record == null) {
			return;
		}

		adapter.select ((int)record.getTag (EventListener.Selected));

	}

}
