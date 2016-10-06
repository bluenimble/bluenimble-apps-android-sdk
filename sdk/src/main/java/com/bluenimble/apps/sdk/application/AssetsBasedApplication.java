package com.bluenimble.apps.sdk.application;

import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.bluenimble.apps.sdk.Lang;
import com.bluenimble.apps.sdk.spec.impls.json.AssetsBasedApplicationSpec;
import com.bluenimble.apps.sdk.ui.utils.Resources;

import java.io.IOException;

public class AssetsBasedApplication extends UIApplication {

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
		if (com.bluenimble.apps.sdk.ui.utils.Resources.exists (path)) {
			return com.bluenimble.apps.sdk.ui.utils.Resources.drawable (this, path);
		}

		try {
			return Drawable.createFromStream (getAssets ().open (spec.id () + Lang.SLASH + Resources.Themes + Lang.SLASH + path), null);
		} catch (IOException e) {
			return null;
		}
	}

}
