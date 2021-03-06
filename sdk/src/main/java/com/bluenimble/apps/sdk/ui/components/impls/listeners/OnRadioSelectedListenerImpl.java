package com.bluenimble.apps.sdk.ui.components.impls.listeners;

import com.bluenimble.apps.sdk.application.UIActivity;
import com.bluenimble.apps.sdk.controller.impls.actions.DefaultActionInstance;
import com.bluenimble.apps.sdk.json.JsonObject;
import com.bluenimble.apps.sdk.spec.ApplicationSpec;
import com.bluenimble.apps.sdk.utils.SpecHelper;

import android.util.Log;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class OnRadioSelectedListenerImpl extends EventListener implements OnCheckedChangeListener {
	
	private static final long serialVersionUID = 4426753434150177456L;

	public OnRadioSelectedListenerImpl (Event event, JsonObject eventSpec) {
		super (event, eventSpec);
	}

	@Override
	public void onCheckedChanged (RadioGroup group, int btnId) {
		ApplicationSpec application = SpecHelper.application (group);
		application.controller ()
			.process (
				DefaultActionInstance.create (event.name (), eventSpec, application, null, group),
				(UIActivity)group.getContext (),
				true
			);
	}
	
}