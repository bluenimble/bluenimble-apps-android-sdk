package com.bluenimble.apps.sdk.ui.effects.impls;

import com.bluenimble.apps.sdk.application.UIActivity;
import com.bluenimble.apps.sdk.controller.DataHolder;
import com.bluenimble.apps.sdk.spec.ApplicationSpec;
import com.bluenimble.apps.sdk.spec.PageSpec;
import com.bluenimble.apps.sdk.ui.effects.Effect;

public class OpenEffect implements Effect {

	private static final long serialVersionUID = 3783743185246914342L;
	
	public static final String Id = "open";

	@Override
	public String id () {
		return Id;
	}

	@Override
	public void apply (UIActivity activity, ApplicationSpec application, PageSpec page, Object spec, DataHolder dh) {
		
		//Intent intent = new Intent (this, TheNewActivity.class);
		
		// open an external component / activity
		/*
		
		Intent intent = new Intent (this, TheNewActivity.class);

        intent.putExtra (EXTRA_MESSAGE, message);

        startActivity (intent);
        
        */
		
	}

}
