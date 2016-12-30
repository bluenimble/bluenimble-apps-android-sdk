package com.bluenimble.apps.sdk.backend.impls.storage;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Environment;

import com.bluenimble.apps.sdk.IOUtils;
import com.bluenimble.apps.sdk.Json;
import com.bluenimble.apps.sdk.Lang;
import com.bluenimble.apps.sdk.backend.Service;
import com.bluenimble.apps.sdk.controller.DataHolder;
import com.bluenimble.apps.sdk.controller.StreamSource;
import com.bluenimble.apps.sdk.json.JsonObject;
import com.bluenimble.apps.sdk.spec.ApplicationSpec;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class StorageService implements Service {

	private static final long serialVersionUID = 2077148821012512850L;

	public interface Spec {
		String Folder   = "folder";
		String Name     = "name";
		String Type     = "type";
		String Verb     = "verb";
		String Data     = "data";
	}

	interface Response {
		String Status   = "status";
	}

	private enum Type {
		EXTERNAL,
		INTERNAL
	}

	private enum Verb {
		GET,
		POST,
		DELETE
	}

	@Override
	public void execute (String id, JsonObject masterSpec, ApplicationSpec application, DataHolder dh, Context appContext) throws Exception {
		application.logger ().info (StorageService.class.getSimpleName () + " ---> Execute", "Service " + id);

		if (masterSpec == null) {
			throw new Exception ("backend service spec not found");
		}

		application.logger ().info (StorageService.class.getSimpleName () + " ---> Execute", "Master Spec : " + masterSpec.toString ());

		final JsonObject spec = masterSpec.duplicate ();

		Verb verb;
		try {
			verb = Verb.valueOf (Json.getString (spec, Spec.Verb, Verb.GET.name ()).toUpperCase ());
		} catch (Exception ex) {
			verb = Verb.GET;
		}

		String name     = Json.getString (spec, Spec.Name);
		if (Lang.isNullOrEmpty (name) && verb != Verb.GET) {
			throw new Exception ("backend service spec not found");
		}

		StreamSource streamSource = dh.stream (id);
		if (streamSource == null) {
			throw new Exception ("No stream passed to this service.");
		}

		if (Lang.isNullOrEmpty (name) && verb == Verb.GET) {
			name        = streamSource.name ();
		}

		Type type       = Type.EXTERNAL;
		String sType    = Json.getString (spec, Spec.Type);
		if (!Lang.isNullOrEmpty (sType)) {
			type = Type.valueOf (sType.toUpperCase ());
		}

		boolean goInternal = (type == Type.INTERNAL);
		if (!goInternal) {
			String state    = Environment.getExternalStorageState ();
			goInternal      = !state.equals (Environment.MEDIA_MOUNTED)
								|| ((verb == Verb.POST || verb == Verb.DELETE) && state.equals (Environment.MEDIA_MOUNTED_READ_ONLY))
								|| (verb == Verb.GET && !state.equals (Environment.MEDIA_MOUNTED_READ_ONLY));
		}

		String sRoot;
		if (goInternal) {
			sRoot = appContext.getFilesDir ().getAbsolutePath ();
		} else {
			sRoot = Environment.getExternalStorageDirectory ().getAbsolutePath () + File.separator + getAppName (appContext);
		}

		String sFolder = Json.getString (spec, Spec.Folder, Lang.BLANK);
		if (!Lang.isNullOrEmpty (sFolder)) {
			if (sFolder.startsWith (Lang.SLASH)) {
				sFolder = sFolder.replaceFirst (Lang.SLASH, Lang.BLANK);
			}
		}
		sRoot += sFolder;

		File folder = new File (sRoot);
		folder.mkdirs ();

		InputStream is = streamSource.stream ();

		File target = new File (folder, name);
		OutputStream os = new FileOutputStream (target);
		IOUtils.copy (is, os);

		IOUtils.closeQuietly (os);
		IOUtils.closeQuietly (is);

		dh.set (Response.Status, true);
	}

	private String getAppName (Context appContext) throws Exception {
		PackageManager packageManager   = appContext.getPackageManager ();
		ApplicationInfo applicationInfo = packageManager.getApplicationInfo (appContext.getApplicationInfo ().packageName, 0);
		return packageManager.getApplicationLabel (applicationInfo).toString ();
	}
}