package com.bluenimble.apps.sdk.json;

import java.io.File;
import java.io.InputStream;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.bluenimble.apps.sdk.Json;
import com.bluenimble.apps.sdk.Lang;

@SuppressWarnings("rawtypes")
public class JsonObject extends JsonAbstractEntity implements Map {

	private static final long serialVersionUID 	= -374323560863553121L;
	
	public static final JsonObject 	Blank 		= new JsonObject (Collections.unmodifiableMap (new HashMap<String, Object> ()));

	private Map<String, Object> 	values;
	
	public JsonObject () {
	}
	
	public JsonObject (String json) throws JsonException {
		this (new JsonParser (json));
	}

	public JsonObject (Map<String, Object> data, boolean resolve) {
		if (data == null) {
			return;
		}
		
		values = data;
		
		if (resolve) {
			resolve ();
		}
	}

	public JsonObject (Map<String, Object> data) {
		this (data, true);
	}
	
	public JsonObject (InputStream stream) throws JsonException {
		this (new JsonParser (stream));
	}

	@SuppressWarnings("unchecked")
	private void resolve () {
		if (values.isEmpty ()) {
			return;
		}
		Iterator<String> keys = values.keySet ().iterator ();
		while (keys.hasNext ()) {
			String key = keys.next ();
			Object o = values.get (key);
			if (o instanceof Map && !(o instanceof JsonObject)) {
				set (key, new JsonObject ((Map)o, true)); 
			} else if (o instanceof List && !(o instanceof JsonArray)) {
				set (key, new JsonArray ((List)o)); 
			}
		}
	}

	public JsonObject(JsonParser jt) throws JsonException {
		char c;
		String key;

		if (jt.nextClean() != '{') {
			throw jt.syntaxError("A JsonObject text must begin with '{'");
		}
		for (;;) {
			c = jt.nextClean();
			switch (c) {
			case 0:
				throw jt.syntaxError("A JsonObject text must end with '}'");
			case '}':
				return;
			default:
				jt.back();
				key = jt.nextValue().toString();
			}

			c = jt.nextClean();
			if (c == '=') {
				if (jt.next() != '>') {
					jt.back();
				}
			} else if (c != ':') {
				throw jt.syntaxError("Expected a ':' after a key");
			}
			set (key, jt.nextValue());

			switch (jt.nextClean()) {
			case ';':
			case ',':
				if (jt.nextClean() == '}') {
					return;
				}
				jt.back();
				break;
			case '}':
				return;
			default:
				throw jt.syntaxError("Expected a ',' or '}'");
			}
		}
	}
	
	public JsonObject duplicate () {
		JsonObject o = new JsonObject ();
		if (values == null || values.isEmpty ()) {
			return o;
		}
		
		Iterator<String> keys = keys ();
		while (keys.hasNext ()) {
			String key = keys.next ();
			o.set (key, duplicate (get (key)));
		}
		return o;
	}
	
	private Object duplicate (Object value) {
		if (value.equals (NULL)) {
			return value;
		}
		if (value instanceof JsonObject) {
			return ((JsonObject)value).duplicate ();
		} else if (value instanceof JsonArray) {
			JsonArray arr = (JsonArray)value;
			JsonArray newArr = new JsonArray ();
			for (int i = 0; i < arr.count (); i++) {
				newArr.add (duplicate (arr.get (i)));
			}
			return newArr;
		} 
		return value;
	}

	@Override
	@SuppressWarnings({ "unchecked" })
	public JsonEntity set (String name, Object value) {
		if (name == null || value == null) {
			return this;
		}
		if (values == null) {
			values = new LinkedHashMap<String, Object>();
		}
		
		if (value instanceof JsonObject || value instanceof JsonArray) {
			values.put (name, value);
		} else if (Map.class.isAssignableFrom (value.getClass ())) {
			values.put (name, new JsonObject ((Map)value));
		} else if (List.class.isAssignableFrom (value.getClass ())) {
			values.put (name, new JsonArray ((List)value));
		} else {
			values.put (name, value);
		}
		return this;
	}

	@Override
	public Object put (Object key, Object value) {
		set (String.valueOf (key), value);
		return value;
	}

	public void remove (String name) {
		if (name == null || values == null) {
			return;
		}
		values.remove(name);
	}

	public Object get (String name) {
		if (name == null || values == null) {
			return null;
		}
		Object v = values.get (name);
		if (v == NULL) {
			return null;
		}
		return v;
	}

	public Iterator<String> keys () {
		if (values == null) {
			return null;
		}
		return values.keySet().iterator();
	}

	@Override
	public Collection<Object> values () {
		if (values == null) {
			return null;
		}
		return values.values ();
	}

	@Override
	public boolean isEmpty () {
		return values == null || values.isEmpty();
	}

	public int count () {
		return values == null ? 0 : values.size();
	}
	
	public Object first () {
		if (values == null) {
			return null;
		}
		Iterator<Object> it = values.values ().iterator ();
		if (it.hasNext ()) {
			return it.next ();
		}
		return null;
	}

	@Override
	public void clear () {
		if (values == null) {
			return;
		}
		Iterator<Object> valuesIter = values ().iterator ();
		while (valuesIter.hasNext()) {
			Object value = valuesIter.next();
			if (value instanceof JsonObject) {
				((JsonObject) value).clear();
			} else if (value instanceof JsonArray) {
				((JsonArray) value).clear();
			}
		}
		values.clear();
		values = null;
	}
	
    public String getString (String name) {
    	Object value = get (name);
    	if (value == null) {
    		return null;
    	}
        return value.toString ();
    }

	private Object getTypedValue (String name) throws JsonException {
		Object value = get (name);
        if (value == null) {
    		throw new JsonException (quote (null, name) + " is null");
    	}
        return value;
	}

    public boolean getBoolean (String name) throws JsonException {
        Object value = getTypedValue (name);
        if (Boolean.FALSE.equals (value) ||
                (value instanceof String &&
                ((String)value).equalsIgnoreCase (FALSE))) {
            return false;
        } else if (Boolean.TRUE.equals (value) ||
                (value instanceof String &&
                ((String)value).equalsIgnoreCase(TRUE))) {
            return true;
        }
        return false;
    }
	
    public double getDouble (String name) throws JsonException {
    	Object value = getTypedValue (name);
        try {
        	return value instanceof Number ?
                    ((Number)value).doubleValue() :
                    Double.valueOf((String)value).doubleValue();
        } catch (Throwable th) {
            throw new JsonException (th, "Value Of [" + quote (null, name) +
                "] is not a number.");
        }
    }

    public int getInt (String name) throws JsonException {
        Object value = getTypedValue (name);
        return value instanceof Number ?
                ((Number)value).intValue() : (int)getDouble(name);
    }

    public long getLong (String name) throws JsonException {
        Object value = getTypedValue (name);
        return value instanceof Number ?
                ((Number)value).longValue() : (long)getDouble (name);
    }

    public JsonArray getArray (String name) throws JsonException {
        Object value = get (name);
        if (value == null) {
        	return null;
    	}
        if (value instanceof JsonArray) {
            return (JsonArray)value;
        }
        throw new JsonException("Value Of [" + quote (null, name) +
                "] is not a JsonArray.");
    }

    public JsonObject getObject (String name) throws JsonException {
        Object value = get (name);
        if (value == null) {
        	return null;
    	}
        if (value instanceof JsonObject) {
            return (JsonObject)value;
        }
        throw new JsonException("Value Of [" + quote (null, name) +
                "] is not a JsonObject.");
    }
    
    public boolean isNull (String name) {
    	Object value = get (name);
        return value == null || NULL.equals (value);
	}

	@Override
	public String toString () {
		try {
			return toString (2);
		} catch (Exception e) {
			throw new RuntimeException (e.getMessage (), e);
		}
	}

	@Override
	public String toString (int indentFactor) {
		StringBuilder buff = new StringBuilder ();
		StringEmitter emitter = new StringEmitter (buff);
		if (indentFactor > 0) {
			emitter.prettify ();
		}
		if (indentFactor == 1) {
			emitter.tab ("  ");
		} 
		write (emitter);
		String s = buff.toString ();
		buff.setLength (0);
		return s;
	}
	
	@Override
	public void write (JsonEmitter emitter) {
		write (emitter, true);
	}
	
	void write (JsonEmitter emitter, boolean root) {
		
		emitter.onStartObject (this, root);
		
		if (!this.isEmpty ()) {
			int counter = 0;
			Iterator<String> keys = this.keys ();
			while (keys.hasNext ()) {
				String key = keys.next ();
				counter++;
				
				emitter.onStartProperty (this, key, counter == this.count ());
				
				Object child = this.get (key);
				if (child instanceof JsonObject) {
					((JsonObject)child).write (emitter, false);
				} else if (child instanceof JsonArray) {
					((JsonArray)child).write (emitter);
				} else {
					emitter.onValue (this, key, child);
				}
				
				emitter.onEndProperty (this, key, counter == this.count ());
				
			}
		}
		
		emitter.onEndObject (this, root);
		
	}
	
	void write (JsonArray a, JsonEmitter emitter) {
		
		if (a == null) {
			return;
		}
		
		emitter.onStartArray (a);
		
		if (!a.isEmpty ()) {
			for (int i = 0; i < a.count (); i++) {
				Object child = a.get (i);
				
				emitter.onStartArrayValue (a, child, (i + 1) == a.count ());
				
				if (child instanceof JsonObject) {
					((JsonObject)child).write (emitter, false);
				} else if (child instanceof JsonArray) {
					write ((JsonArray)child, emitter);
				} else {
					emitter.onValue (a, null, child);
				}
				
				emitter.onEndArrayValue (a, child, (i + 1) == a.count ());

			}
		}
		
		emitter.onEndArray (a);
		
	}

	@Override
	public boolean containsKey (Object key) {
		if (values == null) {
			return false;
		}
		return values.containsKey (key);
	}

	@Override
	public boolean containsValue (Object value) {
		if (values == null) {
			return false;
		}
		return values.containsValue (value);
	}

	@Override
	public Set entrySet () {
		if (values == null) {
			return null;
		}
		return values.entrySet ();
	}

	@Override
	public Object get (Object key) {
		if (values == null) {
			return null;
		}
		return values.get (key);
	}

	@Override
	public Set keySet () {
		if (values == null) {
			return null;
		}
		return values.keySet ();
	}

	@Override
	@SuppressWarnings({ "unchecked" })
	public void putAll (Map m) {
		if (values == null) {
			values = new LinkedHashMap<String, Object>();
		}
		values.putAll (m);
	}

	@Override
	public Object remove (Object key) {
		if (key == null || values == null) {
			return null;
		}
		return values.remove (String.valueOf (key));
	}

	@Override
	public int size () {
		if (values == null) {
			return 0;
		}
		return values.size ();
	}
	
	public Object find (String property, String sep) {
		
		int indexOfSep = property.indexOf (sep);
		
		if (indexOfSep <= 0) {
			return get (property);
		}
		
		Object value = this;
		
		String [] names = Lang.split (property, sep);
		
		for (String name : names) {
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

    public Object find (String exp) {
    	return find (exp, FIND_SEP);
    }
    
	public JsonObject merge (JsonObject data) {
		if (data == null || data.isEmpty ()) {
			return this;
		}

		Iterator<String> keys = data.keys ();
		while (keys.hasNext()) {
			String key = keys.next ();
			Object value = data.get (key);
			
			// if key not found in this object
			if (!containsKey (key)) {
				set (key, value);
				continue;
			}

			// if value for key found but isn't of the same type as the value in
			// data
			Object oldValue = get (key);
			if (!oldValue.getClass ().equals (value.getClass ())) {
				set (key, value);
				continue;
			}

			// if value for key found is of the same type as the value in data
			if (value instanceof JsonObject) {
				JsonObject child = (JsonObject) value;
				if (child.isEmpty ()) {
					set (key, child);
				} else {
					((JsonObject) oldValue).merge (child);
				}
			} else {
				set (key, value);
			}
		}
		return this;
	}

    public JsonObject shrink () {
    	if (isEmpty ()) {
    		return this;
    	}
    	Iterator<String> keys = keys ();
    	while (keys.hasNext ()) {
    		String key = keys.next ();
    		Object o = get (key);
    		if (o instanceof String) {
    			if (Lang.isNullOrEmpty ((String)o)) {
    				keys.remove ();
    			} 
    		} if (o instanceof JsonObject) {
    			JsonObject child = (JsonObject)o;
    			
    			child.shrink ();
    			
    			if (child.isEmpty ()) {
    				keys.remove ();
    			} 
    		} else if (o instanceof JsonArray) {
    			JsonArray child = (JsonArray)o;
    			if (child.isEmpty ()) {
    				keys.remove ();
    			} else {
    				shrinkArray (child);
        			if (child.isEmpty ()) {
        				keys.remove ();
        			} 
    			}
    		} 
    	}
    	return this;
    }
    
    private void shrinkArray (JsonArray child) {
		for (int i = child.count () - 1; i >= 0; i--) {
			Object oArr = child.get (i);
			if (oArr instanceof JsonObject) {
				((JsonObject)oArr).shrink ();
				if (((JsonObject)oArr).isEmpty ()) {
    				child.remove (i);
    			}
			} else if (oArr instanceof JsonArray) {
				shrinkArray ((JsonArray)oArr);
				if (((JsonArray)oArr).isEmpty ()) {
    				child.remove (i);
    			}
			} else if (oArr instanceof String) {
				if (Lang.isNullOrEmpty (((String)oArr))) {
    				child.remove (i);
    			}
			}
		}
    }
    
    public static void main (String [] args) throws Exception {
    	System.out.println (Json.load (new File ("tests/json/master.json")).merge (Json.load (new File ("tests/json/data.json"))).shrink ());
	}
    
}