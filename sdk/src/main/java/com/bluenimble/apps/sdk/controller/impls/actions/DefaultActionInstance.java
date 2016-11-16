package com.bluenimble.apps.sdk.controller.impls.actions;

import android.view.View;

import com.bluenimble.apps.sdk.Json;
import com.bluenimble.apps.sdk.controller.ActionInstance;
import com.bluenimble.apps.sdk.controller.DataHolder;
import com.bluenimble.apps.sdk.json.JsonObject;
import com.bluenimble.apps.sdk.spec.ApplicationSpec;

/**
 * Created by LINVI on 26/10/2016.
 */

public class DefaultActionInstance implements ActionInstance {

	protected String 		eventName;
	protected JsonObject 	eventSpec;
	protected View 			initiator;
	protected DataHolder 	dataHolder;

	protected DefaultActionInstance (String eventName, JsonObject eventSpec, ApplicationSpec application, DataHolder dataHolder, View initiator) {
		this.eventName 	= eventName;
		this.eventSpec 	= eventSpec.duplicate ();
		this.initiator 	= initiator;
		this.dataHolder = dataHolder;
	}

	public static ActionInstance create (String eventName, JsonObject eventSpec, ApplicationSpec application, DataHolder dataHolder, View initiator) {
		return new DefaultActionInstance (eventName, eventSpec, application, dataHolder, initiator);
	}

	@Override
	public String eventName () {
		return eventName;
	}

	@Override
	public JsonObject eventSpec () {
		return eventSpec;
	}

	@Override
	public DataHolder dataHolder () {
		return dataHolder;
	}

	@Override
	public void dataHolder (DataHolder dataHolder) {
		this.dataHolder = dataHolder;
	}

	@Override
	public View initiator () {
		return initiator;
	}

}
