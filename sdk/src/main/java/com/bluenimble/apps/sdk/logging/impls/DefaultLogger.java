package com.bluenimble.apps.sdk.logging.impls;

import java.util.HashMap;
import java.util.Map;

import com.bluenimble.apps.sdk.Lang;
import com.bluenimble.apps.sdk.logging.Logger;

import android.util.Log;

public class DefaultLogger implements Logger {

	private static final long serialVersionUID = -3921264565612845639L;

	protected Level level = Level.Debug;
	
	private static final Map<Level, Integer> Levels = new HashMap<Level, Integer> ();
	static {
		Levels.put (Level.Debug, 0);
		Levels.put (Level.Info, 1);
		Levels.put (Level.Warning, 2);
		Levels.put (Level.Error, 3);
		Levels.put (Level.Severe, 4);
		Levels.put (Level.None, 5);
	}
	
	@Override
	public void setLevel (Level level) {
		this.level = level;
	}

	@Override
	public Level getLevel () {
		return level;
	}
	
	@Override
	public void debug (String where, Object message) {
		if (Levels.get (Level.Debug) < Levels.get (level)) {
			return;
		}
		if (message == null) {
			return;
		}
		Log.d (where, message.toString ());
	}

	@Override
	public void info (String where, Object message) {
		if (Levels.get (Level.Info) < Levels.get (level)) {
			return;
		}
		if (message == null) {
			return;
		}
		Log.i (where, message.toString ());
	}

	@Override
	public void error (String where, Object message) {
		if (Levels.get (Level.Error) < Levels.get (level)) {
			return;
		}
		if (message == null) {
			return;
		}
		Log.e (where, message.toString ());
	}

	@Override
	public void error (String where, Throwable th) {
		if (Levels.get (Level.Error) < Levels.get (level)) {
			return;
		}
		Log.e (where, Lang.BLANK, th);
	}

	@Override
	public void warn (String where, Object message) {
		if (Levels.get (Level.Warning) < Levels.get (level)) {
			return;
		}
		if (message == null) {
			return;
		}
		Log.w (where, message.toString ());
	}

	@Override
	public void severe (String where, Throwable th) {
		if (Levels.get (Level.Severe) < Levels.get (level)) {
			return;
		}
		Log.wtf (where, th);
	}

	@Override
	public void severe (String where, Object message) {
		if (Levels.get (Level.Severe) < Levels.get (level)) {
			return;
		}
		if (message == null) {
			return;
		}
		Log.wtf (where, message.toString ());
	}

}
