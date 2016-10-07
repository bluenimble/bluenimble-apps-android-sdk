package com.bluenimble.apps.sdk.spec.impls.json;

import java.io.File;
import java.io.InputStream;

import com.bluenimble.apps.sdk.ArchiveUtils;
import com.bluenimble.apps.sdk.FileUtils;
import com.bluenimble.apps.sdk.IOUtils;
import com.bluenimble.apps.sdk.Lang;
import com.bluenimble.apps.sdk.application.UIApplication;
import com.bluenimble.apps.sdk.json.JsonObject;

public class ArchiveApplicationSpec extends DiskApplicationSpec {

	private static final long serialVersionUID = -5392390555922025109L;

	public ArchiveApplicationSpec (UIApplication application) throws Exception {

		String appFile = application.getString (UIApplication.Meta.Application, UIApplication.Defaults.Archive);

		File appFolder = new File (getAppsFolder (application), appFile.substring (appFile.lastIndexOf (Lang.SLASH) + 1, appFile.indexOf (Lang.DOT)));
		if (!appFolder.exists ()) {
			unpack (application.getAssets ().open (appFile), appFolder);
		}

		// setup
		setup (appFolder, application);

	}

	public ArchiveApplicationSpec (String id, InputStream stream, UIApplication application) throws Exception {
		// unpack
		File appFolder = new File (getAppsFolder (application), id);
		if (appFolder.exists ()) {
			FileUtils.delete (appFolder);
			appFolder.mkdir ();
		}

		unpack (stream, appFolder);

		// setup
		setup (appFolder, application);
	}

	private void unpack (InputStream stream, File folder) throws Exception {
		try {
			ArchiveUtils.decompress (stream, folder);
		} finally {
			IOUtils.closeQuietly (stream);
		}
	}
	
}
