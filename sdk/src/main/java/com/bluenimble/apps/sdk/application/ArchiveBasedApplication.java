package com.bluenimble.apps.sdk.application;

import com.bluenimble.apps.sdk.spec.impls.json.ArchiveBasedApplicationSpec;

public class ArchiveBasedApplication extends UIApplication {

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
		spec = new ArchiveBasedApplicationSpec (this);
		setLogLevel ();
	}
	
	
}
