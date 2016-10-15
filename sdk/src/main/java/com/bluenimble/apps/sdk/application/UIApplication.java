package com.bluenimble.apps.sdk.application;

import java.util.concurrent.atomic.AtomicInteger;

import com.bluenimble.apps.sdk.logging.Logger.Level;
import com.bluenimble.apps.sdk.spec.ApplicationSpec;
import com.bluenimble.apps.sdk.spec.ViewSize;

import android.app.Application;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;

public abstract class UIApplication extends Application {

	public interface Meta {
		String Archive 			= "bluenimble.app.archive";
		String Application 		= "bluenimble.app.folder";
	}

	public static final String ArchiveExtension = ".app";
	public static final String PageExtension 	= ".json";

	public interface Defaults {
		String Folder 	= "bluenimble";
		String Archive 	= Folder + ArchiveExtension;
	}

	public interface Resources {
		String Apps 		= "apps";

		String Pages 		= "pages";

		String Themes 		= "themes";
		String Images 		= "images";
		String Fonts 		= "fonts";
		String Common 		= "common";

		String App 			= "app.json";
		String Backend 		= "backend.json";
		String Static 		= "static.json";
		String Theme 		= "theme.json";
	}
	
	private static final AtomicInteger NextId = new AtomicInteger (1);
	
	protected 	ApplicationSpec spec;
	
	protected 	static ViewSize screenSize;
	
	@Override
	public void onCreate () {
		super.onCreate ();
		screenSize = getSize (false);
	}

	public static final ViewSize getScreenSize () {
		return screenSize;
	}

	protected void setLogLevel () {
		Level logLevel = Level.Debug;
		try {
			logLevel = Level.valueOf (spec.logLevel ());
		} catch (Exception ex) {
			// ignore
		}
		
		spec.logger ().setLevel (logLevel);
	}
	
	public ApplicationSpec getSpec () {
		return spec;
	}
	
	public ViewSize getSize (boolean indp) {
		DisplayMetrics metrics = new DisplayMetrics ();
		WindowManager wm = (WindowManager) getSystemService (WINDOW_SERVICE);
		wm.getDefaultDisplay ().getMetrics (metrics);
		if (indp) {
			return new ViewSize ( metrics.xdpi, metrics.ydpi );
		}
		return new ViewSize ( metrics.widthPixels, metrics.heightPixels );
	}
	
	protected Bundle bundle () {
		try {
			return getPackageManager ().getApplicationInfo (getPackageName (), PackageManager.GET_META_DATA).metaData;
		} catch (NameNotFoundException e) {
			return null;
		}
	}
	
	public boolean getBoolean (String key, boolean defaultValue) {
		Bundle b = bundle ();
		if (b == null) {
			return defaultValue;
		}
		return b.getBoolean (key, defaultValue);
	}
	
	public String getString (String key, String defaultValue) {
		Bundle b = bundle ();
		if (b == null) {
			return defaultValue;
		}
		return b.getString (key, defaultValue);
	}
	
	public static int newId () {
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
			for (;;) {
		        final int current = NextId.get ();
		        int nextValue = current + 1;
		        if (nextValue > 0x00FFFFFF) {
		        	nextValue = 1;
		        } 
		        if (NextId.compareAndSet (current, nextValue)) {
		            return current;
		        }
		    }
	    } else {
	        return View.generateViewId ();
	    }
	}
	
	protected 	abstract void reload () throws Exception;

	public 		abstract Drawable drawable	(String path);

}
