package com.bluenimble.apps.sdk.ui.components.impls.listeners;

import com.bluenimble.apps.sdk.application.UIActivity;
import com.bluenimble.apps.sdk.controller.ActionProcessor;
import com.bluenimble.apps.sdk.json.JsonObject;
import com.bluenimble.apps.sdk.ui.components.impls.list.DefaultListAdapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewParent;

public class OnPressListenerImpl extends EventListener implements OnClickListener {
	
	private static final long serialVersionUID = 4426753434150177456L;

	public OnPressListenerImpl (Event event, JsonObject eventSpec) {
		super (event, eventSpec);
	}

	@Override
	public void onClick (View view) {
		markAsSelected (view);
		ActionProcessor.process (event.name (), eventSpec, (UIActivity)view.getContext (), view, null);
	}
	
}