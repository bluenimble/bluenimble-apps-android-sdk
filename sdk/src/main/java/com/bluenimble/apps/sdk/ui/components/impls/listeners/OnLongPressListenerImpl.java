package com.bluenimble.apps.sdk.ui.components.impls.listeners;

import com.bluenimble.apps.sdk.application.UIActivity;
import com.bluenimble.apps.sdk.controller.impls.actions.DefaultActionInstance;
import com.bluenimble.apps.sdk.json.JsonObject;
import com.bluenimble.apps.sdk.spec.ApplicationSpec;
import com.bluenimble.apps.sdk.utils.SpecHelper;

import android.view.View;
import android.view.View.OnLongClickListener;

public class OnLongPressListenerImpl extends EventListener implements OnLongClickListener {
	
	private static final long serialVersionUID = 4426753434150177456L;

	public OnLongPressListenerImpl (Event event, JsonObject eventSpec) {
		super (event, eventSpec);
	}

	@Override
	public boolean onLongClick (View view) {
		markAsSelected (view);
		ApplicationSpec application = SpecHelper.application (view);
		application.controller ()
			.process (
				DefaultActionInstance.create (event.name (), eventSpec, application, null, view),
				(UIActivity)view.getContext (),
				true
			);
		return true;
	}
	
}