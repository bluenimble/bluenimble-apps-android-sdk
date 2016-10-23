package com.bluenimble.apps.sdk;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.text.ParseException;
import java.util.Date;
import java.util.Iterator;

import com.bluenimble.apps.sdk.Lang.VariableResolver;
import com.bluenimble.apps.sdk.json.JsonArray;
import com.bluenimble.apps.sdk.json.JsonObject;

public class Json {

	public static JsonObject load (String name) throws Exception {
		return load (Thread.currentThread ().getContextClassLoader (), name);
	}
	
	public static JsonObject load (InputStream stream) throws Exception {
		return new JsonObject (IOUtils.toString (stream));
	}
    
	public static JsonObject load (File file) throws Exception {
		
		if (!file.exists ()) {
			return null;
		}
		
		InputStream is = null;
		try { 
			is = new FileInputStream (file);
			return new JsonObject (IOUtils.toString (is));
		} finally {
			IOUtils.closeQuietly (is);
		}
	}
    
	public static JsonObject load (ClassLoader loader, String name) throws Exception {
		if (Lang.isNullOrEmpty (name)) {
			throw new NullPointerException ("load with no parameters");
		}
		InputStream is = null;
		try { 
			
			if (name.indexOf (Lang.URL_ACCESSOR) > 0) {
				URL url = new URL (name);
				is = url.openStream ();
			} else {
				is = loader.getResourceAsStream (name);
			}
			
			return new JsonObject (IOUtils.toString (is));
			
		} finally {
			if (is != null) {
				try {
					is.close ();
				} catch (IOException ex) {
					ex.printStackTrace (System.err);
				}
			}
		}
	}
    
    public static void store (JsonObject source, File file) throws IOException {
    	if (source == null) {
    		source = new JsonObject ();
    	}
    	OutputStream os = null;
    	try {
    		os = new FileOutputStream (file);
    		IOUtils.copy (new ByteArrayInputStream (source.toString (2).getBytes ()), os);
    	} finally {
    		IOUtils.closeQuietly (os);
    	}
    }

    public static JsonObject getObject (JsonObject source, String name) {
    	if (source == null || Lang.isNullOrEmpty (name)) {
    		return null;
    	}
    	return (JsonObject)source.get (name);
    }

    public static JsonArray getArray (JsonObject source, String name) {
    	if (source == null || Lang.isNullOrEmpty (name)) {
    		return null;
    	}
    	return (JsonArray)source.get (name);
    }

    public static String getString (JsonObject source, String name) {
    	return getString (source, name, null);
    }

    public static String getString (JsonObject source, String name, String defaultValue) {
    	if (source == null || Lang.isNullOrEmpty (name)) {
    		return defaultValue;
    	}
    	String v = source.getString (name);
    	if (v == null) {
    		return defaultValue;
    	}
    	return v;
    }

    public static int getInteger (JsonObject source, String name, int defaultValue) {
    	
    	int value = defaultValue;
    	
    	if (source == null || Lang.isNullOrEmpty (name) || source.get (name) == null) {
    		return value;
    	}

    	String sInteger = String.valueOf (source.get (name));
		if (!Lang.isNullOrEmpty (sInteger)) {
			try {
				value = Integer.parseInt (sInteger.trim ());
			} catch (NumberFormatException nfex) {
				// Ignore
			}
		}
		return value;
    }

    public static long getLong (JsonObject source, String name, long defaultValue) {
    	
    	long value = defaultValue;
    	
    	if (source == null || Lang.isNullOrEmpty (name) || source.get (name) == null) {
    		return value;
    	}

    	String sLong = String.valueOf (source.get (name));
		if (!Lang.isNullOrEmpty (sLong)) {
			try {
				value = Long.parseLong (sLong.trim ());
			} catch (NumberFormatException nfex) {
				// Ignore
			}
		}
		return value;
    }

    public static boolean getBoolean (JsonObject source, String name, boolean defaultValue) {
    	
    	boolean value = defaultValue;
    	
    	if (source == null || Lang.isNullOrEmpty (name) || source.get (name) == null) {
    		return value;
    	}

    	String sBoolean = String.valueOf (source.get (name));
		if (!Lang.isNullOrEmpty (sBoolean)) {
			value = Lang.TrueValues.contains (sBoolean.trim ().toLowerCase ());
		}
		return value;
    }

    public static double getDouble (JsonObject source, String name, double defaultValue) {
    	
    	double value = defaultValue;
    	
    	if (source == null || Lang.isNullOrEmpty (name) || source.get (name) == null) {
    		return value;
    	}

    	String sDouble = String.valueOf (source.get (name));
		if (!Lang.isNullOrEmpty (sDouble)) {
			try {
				value = Double.parseDouble (sDouble.trim ());
			} catch (NumberFormatException nfex) {
				// Ignore
			}
		}
		return value;
    }

    public static float getFloat (JsonObject source, String name, float defaultValue) {
    	float value = defaultValue;
    	
    	if (source == null || Lang.isNullOrEmpty (name) || source.get (name) == null) {
    		return value;
    	}

    	String sFloat = String.valueOf (source.get (name));
		if (!Lang.isNullOrEmpty (sFloat)) {
			try {
				value = Float.parseFloat (sFloat.trim ());
			} catch (NumberFormatException nfex) {
				// Ignore
			}
		}
		return value;
    }

    public static Date getDate (JsonObject source, String name) throws ParseException {
    	
    	if (source == null || Lang.isNullOrEmpty (name) || source.get (name) == null) {
    		return null;
    	}
    	
    	Object oDate = source.get (name);
    	if (oDate instanceof Date) {
    		return (Date)oDate;
    	}
    	
		return Lang.toUTC (String.valueOf (oDate));
		
    }

    public static String escape (String value) {
		if (value == null || value.length () == 0) {
			return Lang.BLANK;
		}
		char b;
		char c = 0;
		int i;
		int len = value.length();
		
        StringBuilder sb = new StringBuilder(len + 4);
		String t;
        
		for (i = 0; i < len; i += 1) {
			b = c;
			c = value.charAt(i);
			switch (c) {
			case '\\':
			case '"':
				sb.append('\\');
				sb.append(c);
				break;
			case '/':
				if (b == '<') {
					sb.append('\\');
				}
				sb.append(c);
				break;
			case '\b':
				sb.append("\\b");
				break;
			case '\t':
				sb.append("\\t");
				break;
			case '\n':
				sb.append("\\n");
				break;
			case '\f':
				sb.append("\\f");
				break;
			case '\r':
				sb.append("\\r");
				break;
			default:
				if (c < ' ' || (c >= '\u0080' && c < '\u00a0')
						|| (c >= '\u2000' && c < '\u2100')) {
					t = "000" + Integer.toHexString(c);
					sb.append("\\u" + t.substring(t.length() - 4));
				} else {
					sb.append(c);
				}
			}
		}
		String s = sb.toString();
        sb.setLength (0);
        sb = null;
        return s;
    }
    
    public static Object find (JsonObject target, String... path) {
		
    	if (path == null || path.length == 0) {
    		return null;
    	}
    	
    	if (path.length == 1 && Lang.DOT.equals (path [0])) {
    		return target;
    	}
    	
		Object value = target;
    	
    	for (String name : path) {
			if (value instanceof JsonObject) {
				value = ((JsonObject)value).get (name);
			} else if (value instanceof JsonArray) {
				value = ((JsonArray)value).get (Integer.valueOf (name));
			} 
			if (!(value instanceof JsonObject) && !(value instanceof JsonArray)) {
				break;
			}
		}
		
		return value;
    }
    
    public static void set (JsonObject target, Object value, String... property) {
    	
    	if (target == null || property == null || property.length == 0) {
    		return;
    	}
    	
    	if (property.length == 1) {
    		target.set (property [0], value);
    		return;
    	}
    	
    	JsonObject o = target;
    	for (int i = 0; i < property.length - 1; i++) {
    		Object child = o.get (property [i]);
    		if (child == null) {
    			child = new JsonObject ();
    			o.set (property [i], child);
    		}
    		
    		if (!(child instanceof JsonObject)) {
    			return;
    		}
    		
    		o = (JsonObject)child;
    	}

    	o.set (property [property.length - 1], value);
    	
    }
    
	public static Object resolve (Object obj, VariableResolver vr) {
		if (obj == null || vr == null) {
			return obj;
		}
		if (obj instanceof JsonObject) {
			JsonObject o = (JsonObject)obj;
			Iterator<String> keys = o.keys ();
			while (keys.hasNext ()) {
				String key = keys.next ();
				o.set (key, resolve (o.get (key), vr));
			}
			return o;
		} else if (obj instanceof JsonArray) {
			JsonArray array = (JsonArray)obj;
			for (int i = 0; i < array.count (); i++) {
				Object resolved = resolve (array.get (i), vr);
				array.remove (i); 
				array.add (i, resolved); 
			}
			return array;
		} else {
			return Lang.resolve (String.valueOf (obj), Lang.ARRAY_OPEN, Lang.ARRAY_CLOSE, vr);
		}
	}
	
	public static boolean isNullOrEmpty (JsonObject o) {
		return o == null || o.isEmpty ();
	}
    
    public static void main (String [] args) {
		JsonObject json = (JsonObject)new JsonObject ().set ("fv", ".45").set ("runtime", new JsonObject ().set ("scripting", new JsonArray ()));
		Json.set (json, "hello", "runtime", "scripting", "spi");    
		System.out.println (Json.getFloat (json, "fv", 0f));
    }
    
}
