package com.bluenimble.apps.sdk.ui.utils;

import com.bluenimble.apps.sdk.Lang;

import android.R;
import android.content.Context;
import android.graphics.drawable.Drawable;

import java.lang.reflect.Field;

public class Resources {

	@SuppressWarnings("deprecation")
	public static Drawable drawable (Context context, String name) {
		return context.getResources ().getDrawable (id (name));
	}

	public static boolean exists (String name) {
		if (Lang.isNullOrEmpty (name)) {
			return false;
		}
		name = name.trim ();
		if (name.indexOf (Lang.DOT) >= 0) {
			name = name.substring (0, name.indexOf (Lang.DOT));
		}
		try {
			Field field = R.drawable.class.getField (name);
			return true;
		} catch (Exception e) {
			// TODO: log style -> warning
			return false;
		}
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
			Field field = R.drawable.class.getField (name);
			return (int) field.get (null);
		} catch (Exception e) {
			// TODO: log style -> warning
			return 0;
		}
	}
	
}
