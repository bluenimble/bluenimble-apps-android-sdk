package com.bluenimble.apps.sdk.ui.components.impls.listeners;

import com.bluenimble.apps.sdk.application.UIActivity;
import com.bluenimble.apps.sdk.controller.impls.actions.DefaultActionInstance;
import com.bluenimble.apps.sdk.json.JsonObject;
import com.bluenimble.apps.sdk.utils.SpecHelper;

import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class OnCheckedListenerImpl extends EventListener implements OnCheckedChangeListener {
	
	private static final long serialVersionUID = 4426753434150177456L;

	public OnCheckedListenerImpl (Event event, JsonObject eventSpec) {
		super (event, eventSpec);
	}

	@Override
	public void onCheckedChanged (CompoundButton view, boolean isChecked) {
		SpecHelper.application (view)
			.controller ()
				.process (
					DefaultActionInstance.create (event.name (), eventSpec, null, view),
					(UIActivity)view.getContext (),
					true
				);
	}
	
}