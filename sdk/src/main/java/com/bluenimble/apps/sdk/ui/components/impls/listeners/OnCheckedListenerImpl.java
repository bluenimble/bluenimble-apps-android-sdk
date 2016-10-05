package com.bluenimble.apps.sdk.ui.components.impls.listeners;

import com.bluenimble.apps.sdk.application.UIActivity;
import com.bluenimble.apps.sdk.controller.ActionProcessor;
import com.bluenimble.apps.sdk.json.JsonObject;

import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class OnCheckedListenerImpl extends EventListener implements OnCheckedChangeListener {
	
	private static final long serialVersionUID = 4426753434150177456L;

	public OnCheckedListenerImpl (Event event, JsonObject eventSpec) {
		super (event, eventSpec);
	}

	@Override
	public void onCheckedChanged (CompoundButton view, boolean isChecked) {
		ActionProcessor.process (event.name (), eventSpec, (UIActivity)view.getContext (), view, null);
	}
	
}