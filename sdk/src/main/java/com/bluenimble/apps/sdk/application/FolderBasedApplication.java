package com.bluenimble.apps.sdk.application;

import com.bluenimble.apps.sdk.spec.impls.json.AssetsBasedApplicationSpec;

import android.util.Log;

public class FolderBasedApplication extends UIApplication {

	@Override
	public void onCreate () {
		super.onCreate ();
		
		try {
			reload ();
		} catch (Exception e) {
			Log.d ("FolderBasedApplication", "Error ", e);
			// TODO: Does Application.onCreate exposes a throws Exception
			throw new RuntimeException (e.getMessage (), e);
		}

	}
	
	@Override
	protected void reload () throws Exception {
		spec = new AssetsBasedApplicationSpec (this);
		setLogLevel ();
	}

}
