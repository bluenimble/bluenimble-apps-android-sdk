package com.bluenimble.apps.sdk.ui.utils;

import com.bluenimble.apps.sdk.Lang;

import android.R;
import android.content.Context;
import android.graphics.drawable.Drawable;

public class Resources {

	@SuppressWarnings("deprecation")
	public static Drawable drawable (Context context, String name) {
		return context.getResources ().getDrawable (id (name));
	}
	
	public static int id (String name) {
		if (Lang.isNullOrEmpty (name)) {
			return 0;
		}
		name = name.trim ();
		if (name.indexOf (Lang.DOT) >= 0) {
			name = name.substring (0, name.indexOf (Lang.DOT));
		}
		try {
			return (int)R.drawable.class.getField (name).get (null);
		} catch (Exception e) {
			// TODO: log style -> warning
			return 0;
		}
	}
	
}
