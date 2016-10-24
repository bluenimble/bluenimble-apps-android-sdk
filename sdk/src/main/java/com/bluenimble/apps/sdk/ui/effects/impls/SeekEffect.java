package com.bluenimble.apps.sdk.ui.effects.impls;

import android.widget.VideoView;

import com.bluenimble.apps.sdk.Lang;

public class SeekEffect extends VideoActionEffect {

	private static final long serialVersionUID = 3783743185246914342L;

	private static final String Id = "seek";

	public SeekEffect() {
		super (Id);
	}

	@Override
	public void process (VideoView video, String spec) {
		if (Lang.isNullOrEmpty (spec)) {
			return;
		}
		int msecs = -1;
		try {
			Integer.valueOf (spec.trim ());
		} catch (NumberFormatException nfex) {

		}
		if (msecs < 0) {
			return;
		}

		video.seekTo (msecs);
	}

}
