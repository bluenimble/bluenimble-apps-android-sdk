package com.bluenimble.apps.sdk.utils;

import android.Manifest;
import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.bluenimble.apps.sdk.application.UIActivity;
import com.bluenimble.apps.sdk.application.UIApplication;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SecurityHelper {

	public static final int NoRequestCode 		= -1;
	public static final int StartupRequestCode 	= 0;

	private static final Map<String, String> Permissions = new HashMap<String, String>();
	static {
		//Permissions.put (Manifest.permission.READ_PHONE_STATE, );
	}

	public static int askPermission (String [] permissions, UIActivity activity) {

		if (permissions == null || permissions.length == 0) {
			return NoRequestCode;
		}

		List<String> anPermissions = new ArrayList<> ();

		for (String p : permissions) {
			String anp = Permissions.get (p);
			if (anp == null) {
				continue;
			}
			if (ContextCompat.checkSelfPermission (activity.getApplication (), anp) == PackageManager.PERMISSION_GRANTED) {
				continue;
			}
			if (anPermissions == null) {
				anPermissions = new ArrayList<> ();
			}
			anPermissions.add (anp);
		}

		if (anPermissions == null || anPermissions.isEmpty ()) {
			return NoRequestCode;
		}

		String [] anps = new String [anPermissions.size ()];

		anPermissions.toArray (anps);

		int request = UIApplication.newId ();

		ActivityCompat.requestPermissions (activity, anps, request);

		return request;

	}

	public static String [] allPermissions (UIActivity activity) {
		PackageInfo info = null;
		try {
			info = activity.getPackageManager ().getPackageInfo (activity.getPackageName (), PackageManager.GET_PERMISSIONS);
		} catch (Exception ex) {
			// ignore
		}
		if (info == null) {
			return null;
		}
		if (info.requestedPermissions == null || info.requestedPermissions.length == 0) {
			return null;
		}
		return info.requestedPermissions;
	}

	public static int askForAllPermissions (UIActivity activity) {
		return askForPermissions (activity, allPermissions (activity));
	}

	private static int askForPermissions (UIActivity activity, String [] anps) {
		if (anps == null || anps.length == 0) {
			return NoRequestCode;
		}
		ActivityCompat.requestPermissions (activity, anps, StartupRequestCode);
		return StartupRequestCode;
	}

}
