package com.bluenimble.apps.sdk.controller;

import android.view.View;

import com.bluenimble.apps.sdk.application.UIActivity;
import com.bluenimble.apps.sdk.json.JsonObject;

import java.io.Serializable;

public interface ActionInstance extends Serializable {

	String 		eventName 	();
	JsonObject 	eventSpec 	();
	View 		initiator 	();
	DataHolder 	dataHolder 	();
	void		dataHolder 	(DataHolder dataHolder);

}
