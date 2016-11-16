package com.bluenimble.apps.sdk.ui.components.impls.listeners;

import com.bluenimble.apps.sdk.application.UIActivity;
import com.bluenimble.apps.sdk.controller.impls.actions.DefaultActionInstance;
import com.bluenimble.apps.sdk.json.JsonObject;
import com.bluenimble.apps.sdk.spec.ApplicationSpec;
import com.bluenimble.apps.sdk.utils.SpecHelper;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

public class OnChangeListenerImpl extends EventListener implements TextWatcher {
	
	private static final long serialVersionUID = 4426753434150177456L;

	private EditText	editText;
	
	public OnChangeListenerImpl (EditText editText, Event event, JsonObject eventSpec) {
		super (event, eventSpec);
		this.editText 	= editText;
	}

	@Override
	public void afterTextChanged (Editable editable) {
		if (!Event.afterTextChanged.equals (event)) {
			return;
		}
		onChangeEvent ();
	}

	@Override
	public void beforeTextChanged (CharSequence text, int start, int count, int after) {
		if (!Event.beforeTextChanged.equals (event)) {
			return;
		}
		onChangeEvent ();
	}

	@Override
	public void onTextChanged (CharSequence text, int start, int before, int count) {
		if (!Event.onTextChanged.equals (event)) {
			return;
		}
		onChangeEvent ();
	}

	private void onChangeEvent () {
		ApplicationSpec application = SpecHelper.application (editText);
		application.controller ()
			.process (
				DefaultActionInstance.create (event.name (), eventSpec, application, null, editText),
				(UIActivity)editText.getContext (),
				true
			);
	}
	
}
