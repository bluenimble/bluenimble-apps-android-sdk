package com.bluenimble.apps.sdk.application;

import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.bluenimble.apps.sdk.spec.impls.json.AssetsBasedApplicationSpec;

public class DiskBasedApplication extends UIApplication {

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


	@Override
	public Drawable drawable (String path) {
		return null;
	}

}
