package com.bluenimble.apps.sdk.ui.effects.impls;

import android.view.View;
import android.widget.VideoView;

import com.bluenimble.apps.sdk.Lang;
import com.bluenimble.apps.sdk.application.UIActivity;
import com.bluenimble.apps.sdk.application.ux.LayerLayout;
import com.bluenimble.apps.sdk.controller.DataHolder;
import com.bluenimble.apps.sdk.spec.ApplicationSpec;
import com.bluenimble.apps.sdk.spec.PageSpec;
import com.bluenimble.apps.sdk.ui.effects.Effect;

public abstract class VideoActionEffect implements Effect {

	private static final long serialVersionUID = 3783743185246914342L;

	private String id;

	public VideoActionEffect (String id) {
		this.id = id;
	}

	@Override
	public String id () {
		return id;
	}

	@Override
	public void apply (UIActivity activity, ApplicationSpec application, PageSpec page, Object spec, View origin, DataHolder dh) {
		
		if (spec == null || !(spec instanceof String)) {
			return;
		}

		String id = (String)spec;

		String remaining = null;

		int indexOfSpace = id.indexOf (Lang.SPACE);
		if (indexOfSpace > 0) {
			remaining = id.substring (indexOfSpace + 1).trim ();
			id = id.substring (0, indexOfSpace).trim ();
		}

		int indexOfDot = id.indexOf (Lang.DOT);
		if (indexOfDot <= 0) {
			return;
		}

		View layerView = activity.findView (id.substring (0, indexOfDot));
		if (layerView == null || !(layerView instanceof LayerLayout)) {
			return;
		}

		View view = ((LayerLayout)layerView).findView (id.substring (indexOfDot + 1));
		if (view == null) {
			return;
		}

		process ((VideoView)view, remaining);

	}

	protected abstract void process (VideoView video, String spec);

}
