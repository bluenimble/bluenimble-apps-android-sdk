package com.bluenimble.apps.sdk.json;

import com.bluenimble.apps.sdk.Json;
import com.bluenimble.apps.sdk.Lang;

public abstract class AbstractEmitter implements JsonEmitter {
	
	private boolean			pretty;
	
	private String tab 		= Lang.TAB;
	private String space 	= Lang.SPACE;
	
	protected int indent;
	
	@Override
	public void onStartObject (JsonObject o, boolean root) {
		write (Lang.OBJECT_OPEN);
		if (pretty) {
			onEndLn ();
			if (o.count () > 0) {
				indent++;
			}
		} 
	}

	@Override
	public void onEndObject (JsonObject o, boolean root) {
		if (pretty) {
			onEndLn ();
		} 
		if (pretty && o.count () > 0) {
			indent--;
		}
		indent ();
		write (Lang.OBJECT_CLOSE);
	}

	@Override
	public void onStartProperty (JsonObject p, String name, boolean isLast) {
		indent ();
		writeName (name);
		if (pretty) {
			write (space);
		}
	}

	@Override
	public void onEndProperty (JsonObject p, String name, boolean isLast) {
		if (!isLast) {
			write (Lang.COMMA);
			if (pretty) {
				onEndLn ();
			} 
		}
	}

	@Override
	public void onStartArray (JsonArray a) {
		write (Lang.ARRAY_OPEN);
		if (pretty) {
			onEndLn ();
			if (a.count () > 0) {
				indent++;
			}
		} 
	}

	@Override
	public void onEndArray (JsonArray a) {
		if (pretty) {
			onEndLn ();
		} 
		if (pretty && a.count () > 0) {
			indent--;
		}
		indent ();
		write (Lang.ARRAY_CLOSE);
	}

	@Override
	public void onValue (JsonEntity p, String name, Object value) {
		if (value == null) {
			write (Lang.NULL);
		} else {
			write (Lang.QUOT).write (Json.escape (String.valueOf (value))).write (Lang.QUOT);
		}
	}

	protected void writeName (String name) {
		write (Lang.QUOT).write (name).write (Lang.QUOT).write (Lang.COLON);
	}

	protected void onEndLn () {
		write (Lang.ENDLN);
	}

	@Override
	public void onStartArrayValue (JsonArray array, Object value, boolean isLast) {
		indent ();
	}

	@Override
	public void onEndArrayValue (JsonArray array, Object value, boolean isLast) {
		if (!isLast) {
			write (Lang.COMMA);
			if (pretty) {
				onEndLn ();
			} 
		}
	}
	
	public AbstractEmitter prettify () {
		pretty = true;
		return this; 
	}
	
	public AbstractEmitter tab (String tab) {
		this.tab = tab;
		return this;
	}
	
	private void indent () {
		if (indent <= 0) {
			return;
		}
		for (int i = 0; i < indent; i++) {
			write (tab);	
		}
	}
	
}
