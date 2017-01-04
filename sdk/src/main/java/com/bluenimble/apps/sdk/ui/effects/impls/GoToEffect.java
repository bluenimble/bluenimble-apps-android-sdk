package com.bluenimble.apps.sdk.ui.effects.impls;

import android.content.Intent;
import android.view.View;

import com.bluenimble.apps.sdk.application.UIActivity;
import com.bluenimble.apps.sdk.controller.DataHolder;
import com.bluenimble.apps.sdk.spec.ApplicationSpec;
import com.bluenimble.apps.sdk.spec.PageSpec;
import com.bluenimble.apps.sdk.ui.effects.Effect;

public class GoToEffect implements Effect {

	private static final long serialVersionUID = 3783743185246914342L;

	private static final String Id = "goto";

	private final static String Native = "native:";

	@Override
	public String id () {
		return Id;
	}

	@Override
	public void apply (UIActivity activity, ApplicationSpec application, PageSpec page, Object spec, View origin, DataHolder dh) {
		
		if (spec == null || !(spec instanceof String)) {
			return;
		}

		String pageId = (String)spec;
		if (pageId.startsWith (Native)) {
			pageId = pageId.substring (Native.length ());

			Intent intent = new Intent (activity, UIActivity.class);
			intent.putExtra (UIActivity.Exchange.Page, pageId);
			intent.putExtra (UIActivity.Exchange.Dh, dh);

			activity.startActivity (intent);

			return;
		}
		
		PageSpec nextPage = application.page (pageId);
		if (nextPage == null) {
			// TODO log
			return;
		}
		
		application.renderer ().render (nextPage, activity, dh);
		
	}

}
