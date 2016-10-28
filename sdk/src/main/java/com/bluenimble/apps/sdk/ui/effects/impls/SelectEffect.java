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
		
		// this effect is applicable only if origin ( the event trigger ) view is part of a record of a RecyclerView

		View vRecord = activity.ancestor (origin, RecyclerView.class, true);
		if (vRecord == null) {
			return;
		}

		application.logger ().debug (SelectEffect.class.getSimpleName (), "Record found: " + vRecord);

		RecyclerView list = (RecyclerView)vRecord.getParent ();

		DefaultListAdapter adapter = (DefaultListAdapter)list.getAdapter ();

		application.logger ().debug (SelectEffect.class.getSimpleName (), "\tPosition: " + list.getLayoutManager ().getPosition (vRecord));

		adapter.select (list.getLayoutManager ().getPosition (vRecord));

	}

}
