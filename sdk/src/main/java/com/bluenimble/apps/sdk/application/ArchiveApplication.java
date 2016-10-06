package com.bluenimble.apps.sdk.application;

import android.graphics.drawable.Drawable;

import com.bluenimble.apps.sdk.spec.impls.json.ArchiveApplicationSpec;

public class ArchiveApplication extends UIApplication {

	@Override
	public void onCreate () {
		super.onCreate ();
		
		try {
			reload ();
		} catch (Exception e) {
			// TODO: Does Application.onCreate exposes a throws Exception
			throw new RuntimeException (e.getMessage (), e);
		}
				
	}

	@Override
	protected void reload () throws Exception {
		spec = new ArchiveApplicationSpec(this);
		setLogLevel ();
	}

	@Override
	public Drawable drawable (String path) {
		return null;
	}

}