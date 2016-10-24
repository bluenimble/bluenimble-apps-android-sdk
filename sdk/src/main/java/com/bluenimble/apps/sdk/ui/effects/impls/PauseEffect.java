package com.bluenimble.apps.sdk.ui.effects.impls;

import android.widget.VideoView;

public class PauseEffect extends VideoActionEffect {

	private static final long serialVersionUID = 3783743185246914342L;

	private static final String Id = "pause";

	public PauseEffect() {
		super (Id);
	}

	@Override
	public void process (VideoView video, String spec) {
		if (!video.isPlaying ()) {
			return;
		}
		video.pause ();
	}

}
