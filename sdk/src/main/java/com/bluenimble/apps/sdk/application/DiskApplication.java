package com.bluenimble.apps.sdk.application;

import android.graphics.drawable.Drawable;
import android.util.Log;

import com.bluenimble.apps.sdk.spec.impls.json.AssetsApplicationSpec;
import com.bluenimble.apps.sdk.spec.impls.json.DiskApplicationSpec;

public class DiskApplication extends UIApplication {

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
		spec = new DiskApplicationSpec (this);
		setLogLevel ();
	}


	@Override
	public Drawable drawable (String path) {
		return null;
	}

}
