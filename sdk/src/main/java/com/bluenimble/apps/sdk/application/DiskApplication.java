package com.bluenimble.apps.sdk.application;

import android.graphics.drawable.Drawable;
import android.util.Log;

import com.bluenimble.apps.sdk.IOUtils;
import com.bluenimble.apps.sdk.Lang;
import com.bluenimble.apps.sdk.spec.impls.json.DiskApplicationSpec;
import com.bluenimble.apps.sdk.utils.AppResources;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

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
		if (AppResources.exists (path)) {
			return AppResources.drawable (this, path);
		}

		File file = new File (getFilesDir (), spec.id () + Lang.SLASH + Resources.Themes + Lang.SLASH + path);
		if (!file.exists () || !file.isFile ()) {
			return null;
		}

		String name = path.substring (path.lastIndexOf (Lang.SLASH) + 1, path.indexOf (Lang.DOT));

		InputStream stream = null;
		try {
			stream = new FileInputStream (file);
			return Drawable.createFromStream (stream, name);
		} catch (IOException e) {
			return null;
		} finally {
			IOUtils.closeQuietly (stream);
		}
	}

}
