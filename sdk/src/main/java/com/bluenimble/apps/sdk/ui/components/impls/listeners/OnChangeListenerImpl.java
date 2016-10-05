package com.bluenimble.apps.sdk.ui.components.impls.listeners;

import com.bluenimble.apps.sdk.application.UIActivity;
import com.bluenimble.apps.sdk.controller.ActionProcessor;
import com.bluenimble.apps.sdk.json.JsonObject;

import android.text.Editable;
import android.text.TextWatcher;
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
		ActionProcessor.process (event.name (), eventSpec, (UIActivity)editText.getContext (), editText, null);
	}

	@Override
	public void beforeTextChanged (CharSequence text, int start, int count, int after) {
		if (!Event.beforeTextChanged.equals (event)) {
			return;
		}
		ActionProcessor.process (event.name (), eventSpec, (UIActivity)editText.getContext (), editText, null);
	}

	@Override
	public void onTextChanged (CharSequence text, int start, int before, int count) {
		if (!Event.onTextChanged.equals (event)) {
			return;
		}
		ActionProcessor.process (event.name (), eventSpec, (UIActivity)editText.getContext (), editText, null);
	}
	
}
