package com.bluenimble.apps.sdk.ui.effects.impls;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.bluenimble.apps.sdk.Lang;
import com.bluenimble.apps.sdk.application.UIActivity;
import com.bluenimble.apps.sdk.controller.DataHolder;
import com.bluenimble.apps.sdk.json.JsonArray;
import com.bluenimble.apps.sdk.spec.ApplicationSpec;
import com.bluenimble.apps.sdk.spec.PageSpec;
import com.bluenimble.apps.sdk.ui.components.impls.list.DefaultListAdapter;
import com.bluenimble.apps.sdk.ui.components.impls.listeners.EventListener;
import com.bluenimble.apps.sdk.ui.effects.Effect;

public class DeleteEffect implements Effect {

	private static final long serialVersionUID = 3783743185246914342L;
	
	private static final String Id = "delete";

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

		View list = activity.component (gridId.substring (0, indexOfDot), gridId.substring (indexOfDot + 1));
		if (list == null || !(list instanceof RecyclerView)) {
			return;
		}

		DefaultListAdapter adapter = (DefaultListAdapter)((RecyclerView)list).getAdapter ();

		adapter.deleteSelected ();

	}

}
