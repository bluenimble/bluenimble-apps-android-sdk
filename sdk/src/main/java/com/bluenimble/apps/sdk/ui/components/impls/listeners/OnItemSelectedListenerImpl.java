package com.bluenimble.apps.sdk.ui.components.impls.listeners;

import com.bluenimble.apps.sdk.application.UIActivity;
import com.bluenimble.apps.sdk.controller.impls.actions.DefaultActionInstance;
import com.bluenimble.apps.sdk.json.JsonObject;
import com.bluenimble.apps.sdk.utils.SpecHelper;

import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;

public class OnItemSelectedListenerImpl extends EventListener implements OnItemSelectedListener {

	private static final long serialVersionUID = 4160372040828362861L;
	

	public OnItemSelectedListenerImpl (Event event, JsonObject eventSpec) {
		super (event, eventSpec);
	}

	@Override
	public void onItemSelected (AdapterView<?> parent, View group, int position, long arg3) {
		SpecHelper.application (group)
			.controller ()
				.process (
					DefaultActionInstance.create (event.name (), eventSpec, null, group),
					(UIActivity)group.getContext (),
					true
				);
	}

	@Override
	public void onNothingSelected (AdapterView<?> parent) {
		
	}	
}