package com.bluenimble.apps.sdk.application;

import android.graphics.drawable.Drawable;
import android.util.Log;

import com.bluenimble.apps.sdk.Lang;
import com.bluenimble.apps.sdk.spec.impls.json.AssetsApplicationSpec;
import com.bluenimble.apps.sdk.utils.AppResources;

import java.io.IOException;

public class AssetsApplication extends UIApplication {

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
		spec = new AssetsApplicationSpec(this);
		setLogLevel ();
	}

	@Override
	public Drawable drawable (String path) {
		if (AppResources.exists (path)) {
			return AppResources.drawable (this, path);
		}

		try {
			return Drawable.createFromStream (getAssets ().open (spec.id () + Lang.SLASH + Resources.Themes + Lang.SLASH + path), null);
		} catch (IOException e) {
			return null;
		}
	}

}
