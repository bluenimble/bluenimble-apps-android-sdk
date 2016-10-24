package com.bluenimble.apps.sdk.ui.components.impls.media;

import android.media.MediaPlayer;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.VideoView;

import com.bluenimble.apps.sdk.Lang;
import com.bluenimble.apps.sdk.application.UIActivity;
import com.bluenimble.apps.sdk.application.UIApplication;
import com.bluenimble.apps.sdk.controller.DataHolder;
import com.bluenimble.apps.sdk.controller.StreamSource;
import com.bluenimble.apps.sdk.spec.ApplicationSpec;
import com.bluenimble.apps.sdk.spec.BindingSpec;
import com.bluenimble.apps.sdk.spec.ComponentSpec;
import com.bluenimble.apps.sdk.spec.LayerSpec;
import com.bluenimble.apps.sdk.utils.SpecHelper;
import com.bluenimble.apps.sdk.utils.StorageHelper;

import java.io.File;

public class VideoFactory extends AbstractMediaFactory {

	private static final long serialVersionUID = 8437367345924192857L;
	
	private static final String Id = "video";

	interface Custom {
		String Controls = "controls";
	}

	public VideoFactory (ApplicationSpec application) {
		super (application);
	}

	@Override
	public String id () {
		return Id;
	}
	
	@Override
	public View create (UIActivity activity, ViewGroup group, LayerSpec layer, ComponentSpec spec, DataHolder dh) {

		VideoView video = new VideoView (activity);

		if (SpecHelper.getBoolean (spec, Custom.Controls, true)) {
			video.setMediaController (new MediaController (activity));
		}

		return applyStyle (group, video, spec, dh);
	}

	@Override
	public void bind (ComponentSpec.Binding binding, View view, ApplicationSpec application, ComponentSpec spec, DataHolder dh) {
		
		if (view == null || !(view instanceof VideoView)) {
			// TODO: log
			return;
		}
		
		BindingSpec bindingSpec = spec.binding (binding);
		if (bindingSpec == null) {
			return;
		}

		final VideoView video = (VideoView)view;
		
		switch (binding) {
			case Set:
				if (dh == null) {
					// default image
					return;
				}
				Object value = dh.valueOf (application, bindingSpec);
				if (value == null) {
					return;
				}
				String url = value.toString ().trim ();

				if (url.startsWith (Protocol.Service)) {
					/*StreamSource ss = dh.stream (url.substring (Protocol.Service.length ()));

					// store async
					File videoFile = StorageHelper.put (view.getContext (), ss, null, null);

					video.setVideoURI (Uri.fromFile (videoFile));*/

					// TODO investigate a way how to stream

				} else if (url.startsWith (Protocol.File) || url.startsWith (Protocol.Http) || url.startsWith (Protocol.Https)) {
					video.setVideoURI (Uri.parse ((url)));
				} else {
					if (application.isDiskBased ()) {
						video.setVideoURI (
							Uri.fromFile (
								new File (
									UIApplication.Resources.Apps + Lang.SLASH + application.id () + Lang.SLASH +
									UIApplication.Resources.Themes + Lang.SLASH + url
								)
							)
						);
					} else {
						video.setVideoURI (
							Uri.parse (
								Assets + application.id () + Lang.SLASH + UIApplication.Resources.Themes + Lang.SLASH + url
							)
						);
					}
				}
				video.setOnPreparedListener (new MediaPlayer.OnPreparedListener () {
					public void onPrepared (MediaPlayer mediaPlayer) {
						video.start ();
					}
				});
				break;
			case Get:
				break;
			default:
				break;
		}
		
	}

}
