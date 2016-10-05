package com.bluenimble.apps.sdk.logging;

import java.io.Serializable;

public interface Logger extends Serializable {
	
	enum Level {
		Debug,
		Info,
		Warning,
		Error,
		Severe,
		None
	}
	
	void 	setLevel(Level level);
	Level 	getLevel();

	void 	debug(String where, Object message);
	void 	info(String where, Object message);
	void 	warn(String where, Object message);
	void 	error(String where, Throwable th);
	void 	error(String where, Object message);
	void 	severe(String where, Throwable th);
	void 	severe(String where, Object message);
	
}
