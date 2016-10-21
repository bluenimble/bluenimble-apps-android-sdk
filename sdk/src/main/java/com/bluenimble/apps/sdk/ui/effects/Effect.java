package com.bluenimble.apps.sdk.ui.effects;

import android.view.View;

import java.io.Serializable;

import com.bluenimble.apps.sdk.application.UIActivity;
import com.bluenimble.apps.sdk.controller.DataHolder;
import com.bluenimble.apps.sdk.spec.ApplicationSpec;
import com.bluenimble.apps.sdk.spec.PageSpec;

public interface Effect extends Serializable {

	String 	id		();
	void 	apply	(UIActivity activity, ApplicationSpec application, PageSpec page, Object spec, View origin, DataHolder result);
	
}
