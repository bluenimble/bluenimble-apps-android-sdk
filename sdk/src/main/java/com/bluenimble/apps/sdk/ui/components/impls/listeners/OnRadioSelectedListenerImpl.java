package com.bluenimble.apps.sdk.ui.components.impls.listeners;

import com.bluenimble.apps.sdk.application.UIActivity;
import com.bluenimble.apps.sdk.controller.ActionProcessor;
import com.bluenimble.apps.sdk.json.JsonObject;

import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class OnRadioSelectedListenerImpl extends EventListener implements OnCheckedChangeListener {
	
	private static final long serialVersionUID = 4426753434150177456L;

	public OnRadioSelectedListenerImpl (Event event, JsonObject eventSpec) {
		super (event, eventSpec);
	}

	@Override
	public void onCheckedChanged (RadioGroup group, int btnId) {
		ActionProcessor.process (event.name (), eventSpec, (UIActivity)group.getContext (), group, null);
	}
	
}