package com.bluenimble.apps.sdk.ui.effects.impls;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.bluenimble.apps.sdk.Lang;
import com.bluenimble.apps.sdk.application.UIActivity;
import com.bluenimble.apps.sdk.controller.DataHolder;
import com.bluenimble.apps.sdk.spec.ApplicationSpec;
import com.bluenimble.apps.sdk.spec.PageSpec;
import com.bluenimble.apps.sdk.ui.components.impls.list.DefaultListAdapter;
import com.bluenimble.apps.sdk.ui.effects.Effect;

public class EchoEffect implements Effect {

	private static final long serialVersionUID = 3783743185246914342L;
	
	private static final String Id = "echo";

	@Override
	public String id () {
		return Id;
	}

	@Override
	public void apply (UIActivity activity, ApplicationSpec application, PageSpec page, Object spec, View origin, DataHolder dh) {
		
		if (spec == null) {
			return;
		}

		Toast.makeText (activity, spec.toString (), Toast.LENGTH_SHORT).show ();

	}

}
