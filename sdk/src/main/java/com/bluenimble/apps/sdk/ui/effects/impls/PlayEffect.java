package com.bluenimble.apps.sdk.ui.effects.impls;

import android.view.View;
import android.widget.Toast;
import android.widget.VideoView;

import com.bluenimble.apps.sdk.Lang;
import com.bluenimble.apps.sdk.application.UIActivity;
import com.bluenimble.apps.sdk.controller.DataHolder;
import com.bluenimble.apps.sdk.spec.ApplicationSpec;
import com.bluenimble.apps.sdk.spec.PageSpec;
import com.bluenimble.apps.sdk.ui.effects.Effect;

public class PlayEffect extends VideoActionEffect {

	private static final long serialVersionUID = 3783743185246914342L;
	
	private static final String Id = "play";

	public PlayEffect () {
		super (Id);
	}

	@Override
	public void process (VideoView video, String spec) {
		if (video.isPlaying ()) {
			return;
		}
		video.start ();
	}

}
