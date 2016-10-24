package com.bluenimble.apps.sdk.utils;

import android.content.Context;
import android.os.Environment;

import com.bluenimble.apps.sdk.IOUtils;
import com.bluenimble.apps.sdk.Lang;
import com.bluenimble.apps.sdk.controller.StreamSource;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class StorageHelper {

	interface PutCallback {
		void created 	(File file);
		void stored 	(File file);
		void error 		(Exception ex);
	}

	interface GetCallback {
		void done 	(StreamSource ss);
		void error 	(Exception ex);
	}

	private static final String Downloads = "downloads";

	/* Checks if external storage is available for read and write */
	public static boolean isExternalStorageWritable () {
		String state = Environment.getExternalStorageState ();
		if (Environment.MEDIA_MOUNTED.equals (state)) {
			return true;
		}
		return false;
	}

	/* Checks if external storage is available to at least read */
	public static boolean isExternalStorageReadable () {
		String state = Environment.getExternalStorageState ();
		if (Environment.MEDIA_MOUNTED.equals(state) ||
				Environment.MEDIA_MOUNTED_READ_ONLY.equals (state)) {
			return true;
		}
		return false;
	}

	public static File put (Context context, StreamSource source, String sFolder, PutCallback callback) {
		File folder = null;

		File externalRoot = Environment.getExternalStoragePublicDirectory (Environment.DIRECTORY_DOWNLOADS);

		if (isExternalStorageWritable () && externalRoot.getFreeSpace () > 2 * source.length ()) {
			folder = (sFolder == null ? externalRoot : new File (externalRoot, sFolder));
		} else {
			folder = new File (context.getFilesDir (), Downloads + (sFolder == null ? Lang.BLANK : Lang.SLASH + sFolder));
		}

		File file = new File (folder, source.name ());

		if (!folder.exists ()) {
			folder.mkdirs ();
		}

		try {
			file.createNewFile ();
		} catch (Exception ex) {
			if (callback != null) {
				callback.error (ex);
			}
			return file;
		}

		if (callback != null) {
			callback.created (file);
		}

		OutputStream out = null;
		try {
			out = new FileOutputStream (file);
			IOUtils.copy (source.stream (), out);
			if (callback != null) {
				callback.stored (file);
			}
		} catch (Exception ex) {
			if (callback != null) {
				callback.error (ex);
			}
			return file;
		} finally {
			IOUtils.closeQuietly (out);
			IOUtils.closeQuietly (source.stream ());
		}

		return file;
	}

	public static StreamSource get (Context context, String sFile, GetCallback callback) {
		String sFileName 	= sFile.substring (sFile.lastIndexOf (Lang.SLASH) + 1);

		String sFolder 	= sFile.substring (0, sFile.lastIndexOf (Lang.SLASH));
		String sRoot 	= sFolder.substring (0, sFolder.indexOf (Lang.SLASH));
		sFolder 		= sFolder.substring (sFolder.indexOf (Lang.SLASH) + 1);

		File folder = new File (Environment.getExternalStoragePublicDirectory (sRoot), sFolder);

		final File file = new File (folder, sFileName);

		InputStream input = null;
		try {
			input = new FileInputStream (new File (folder, sFileName));
		} catch (Exception ex) {
			if (callback != null) {
				callback.error (ex);
			}
			return null;
		}

		final InputStream fInput = input;

		StreamSource ss = new StreamSource () {
			@Override
			public String id () {
				return file.getAbsolutePath ();
			}

			@Override
			public String name () {
				return file.getName ();
			}

			@Override
			public String contentType () {
				return null;
			}

			@Override
			public long length () {
				return file.length ();
			}

			@Override
			public InputStream stream () {
				return fInput;
			}

			@Override
			public void close () {
				IOUtils.closeQuietly (fInput);
			}
		};

		if (callback != null) {
			callback.done (ss);
		}

		return ss;

	}

}
