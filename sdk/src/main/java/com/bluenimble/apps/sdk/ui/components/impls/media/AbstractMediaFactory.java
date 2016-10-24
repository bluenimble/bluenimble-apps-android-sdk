package com.bluenimble.apps.sdk.ui.components.impls.media;

import com.bluenimble.apps.sdk.spec.ApplicationSpec;
import com.bluenimble.apps.sdk.ui.components.AbstractComponentFactory;

public abstract class AbstractMediaFactory extends AbstractComponentFactory {

	private static final long serialVersionUID = 8437367345924192857L;

	protected static final String Assets = "file:///android_asset/";

	protected interface Protocol {
		String Service 	= "service://";
		String File 	= "file://";
		String Http 	= "http://";
		String Https 	= "https://";
	}

	protected AbstractMediaFactory (ApplicationSpec application) {
		// create the downloader
		MediaDownloader.create (application);
	}

}
