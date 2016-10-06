package com.bluenimble.apps.sdk.spec.impls.json;

import java.io.InputStream;

import com.bluenimble.apps.sdk.IOUtils;
import com.bluenimble.apps.sdk.application.UIApplication;

import android.content.res.AssetManager;

public class ArchiveApplicationSpec extends JsonBasedApplicationSpec {

	private static final long serialVersionUID = -5392390555922025109L;

	public ArchiveApplicationSpec(UIApplication application) throws Exception {
		
		AssetManager assetManager = application.getAssets ();
		
		InputStream stream = null;
		try {
			stream = assetManager.open (application.getString (UIApplication.Meta.Archive, UIApplication.Defaults.Archive));
			if (stream == null) {
				throw new Exception ("application spec file not found in assets/bluenimble [app.json]");
			}
			
			// TODO read archive
			
		} finally {
			IOUtils.closeQuietly (stream);
		}
	}
	
}
