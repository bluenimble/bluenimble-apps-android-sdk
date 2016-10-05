package com.bluenimble.apps.sdk.json;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import com.bluenimble.apps.sdk.Lang;

public class JsonArray extends JsonAbstractEntity implements List<Object> {

	private static final long serialVersionUID = 5969290028072204587L;

	public static final JsonArray 	Blank 		= new JsonArray (Collections.unmodifiableList (new ArrayList<Object> ()));

	private List<Object> values;

	public JsonArray () {
	}

	public JsonArray (String json) throws JsonException {
		this (new JsonParser (json));
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public JsonArray (List<Object> values) {
		if (values == null) {
			return;
		}
		this.values = new ArrayList<Object> ();
		for (Object o : values) {
			if (o instanceof JsonObject || o instanceof JsonArray) {
				this.values.add (o);
			} else if (o instanceof Map) {
				this.values.add (new JsonObject ((Map)o, true));
			} else if (o instanceof List) {
				this.values.add (new JsonArray ((List)o));
			} else {
				this.values.add (o);
			}
		}
	}

	public JsonArray (JsonParser jt) throws JsonException {
		char c = jt.nextClean();
		char q;
		if (c == '[') {
			q = ']';
		} else if (c == '(') {
			q = ')';
		} else {
			throw jt.syntaxError("A JsonArray text must start with '['");
		}
		if (jt.nextClean() == ']') {
			return;
		}
		jt.back();
		for (;;) {
			if (jt.nextClean() == ',') {
				jt.back();
				add (JsonEntity.NULL);
			} else {
				jt.back();
				add (jt.nextValue());
			}
			c = jt.nextClean();
			switch (c) {
			case ';':
			case ',':
				if (jt.nextClean() == ']') {
					return;
				}
				jt.back();
				break;
			case ']':
			case ')':
				if (q != c) {
					throw jt.syntaxError("Expected a '" + new Character(q)
							+ "'");
				}
				return;
			default:
				throw jt.syntaxError("Expected a ',' or ']'");
			}
		}
	}
	
	public int count () {
		if (values == null) {
			return 0;
		}
		return values.size ();
	}
	
	@Override
	public boolean add (Object value) {
		if (values == null) {
			values = new ArrayList<Object> ();
		}
		return values.add (value);
	}
	
	@Override
	public JsonEntity set (String name, Object value) {
		add (value);
		return this;
	}

	@Override
	public Object get (int index) {
		if (values == null) {
			return null;
		}
		Object v = values.get (index);
		if (v == NULL) {
			return null;
		}
		return v;
	}

	@Override
	public void clear () {
		if (values == null) {
			return;
		}
		
		for (int i = 0; i < count (); i++) {
			Object value = values.get (i);
			if (value instanceof JsonObject) {
				((JsonObject)value).clear ();
			} else if (value instanceof JsonArray) {
				((JsonArray)value).clear ();
			}
		}
		values.clear ();
		values = null;
		
	}
	
	@Override
	public boolean remove (Object obj) {
		if (values == null) {
			return false;
		}
		return values.remove (obj);
	}
	
	@Override
	public Object remove (int index) {
		if (values == null) {
			return null;
		}
		return values.remove (index);
	}
	
	public boolean isNull (int index) {
		return JsonEntity.NULL.equals (get (index));
	}

    @Override
	public String toString () {
        try {
            return '[' + join (",") + ']';
        } catch (Exception e) {
            return null;
        }
    }

    public String join (String separator) throws JsonException {
        int len = count ();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < len; i += 1) {
            if (i > 0) {
                sb.append(separator);
            }
            sb.append (valueToString (get(i)));
        }
        String s = sb.toString();
        sb.setLength (0);
        sb = null;
        return s;
    }
    
    public JsonObject toObject (String field, String altField) {
    	if (Lang.isNullOrEmpty (field) || isEmpty ()) {
    		return new JsonObject ();
    	}
    	
    	JsonObject result = new JsonObject ();
    	
    	for (int i = 0; i < count (); i++) {
    		Object v = get (i);
    		if (v instanceof JsonObject) {
    			JsonObject o = (JsonObject)v;
    			
    			String keyField = field;
    			
    			Object key = o.get (field);
    			if (key == null && !Lang.isNullOrEmpty (altField)) {
        			key = o.get (altField);
        			keyField = altField;
    			}
    			if (key == null) {
        			continue;
    			}
    			
    			result.set (key.toString (), o);
    			o.remove (keyField);
    		}
    	}
    	
    	return result;
    	
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
		write (this, emitter);
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
					((JsonArray)child).write (emitter);
				} else {
					emitter.onValue (a, null, child);
				}
				
				emitter.onEndArrayValue (a, child, (i + 1) == a.count ());

			}
		}
		
		emitter.onEndArray (a);
		
	}

	@Override
	public void add (int index, Object element) {
		if (values == null) {
			values = new ArrayList<Object> ();
		}
		values.add (index, element);
	}

	@Override
	public boolean addAll (Collection<? extends Object> c) {
		if (values == null) {
			values = new ArrayList<Object> ();
		}
		return values.addAll (c);
	}

	@Override
	public boolean addAll (int index, Collection<? extends Object> c) {
		if (values == null) {
			values = new ArrayList<Object> ();
		}
		return values.addAll (index, c);
	}

	@Override
	public boolean contains (Object o) {
		if (values == null) {
			return false;
		}
		return values.contains (o);
	}

	@Override
	public boolean containsAll (Collection<?> c) {
		if (values == null) {
			return false;
		}
		return values.containsAll (c);
	}

	@Override
	public int indexOf (Object o) {
		if (values == null) {
			return -1;
		}
		return values.indexOf (o);
	}

	@Override
	public boolean isEmpty () {
		if (values == null) {
			return true;
		}
		return values.isEmpty ();
	}

	@Override
	public Iterator<Object> iterator () {
		if (values == null) {
			return null;
		}
		return values.iterator ();
	}

	@Override
	public int lastIndexOf (Object o) {
		if (values == null) {
			return -1;
		}
		return values.lastIndexOf (o);
	}

	@Override
	public ListIterator<Object> listIterator () {
		if (values == null) {
			return null;
		}
		return values.listIterator ();
	}

	@Override
	public ListIterator<Object> listIterator (int index) {
		if (values == null) {
			return null;
		}
		return values.listIterator (index);
	}

	@Override
	public boolean removeAll (Collection<?> c) {
		if (values == null) {
			return false;
		}
		return values.removeAll (c);
	}

	@Override
	public boolean retainAll (Collection<?> c) {
		if (values == null) {
			return false;
		}
		return values.retainAll (c);
	}

	@Override
	public Object set (int index, Object element) {
		if (values == null) {
			values = new ArrayList<Object> ();
		}
		return values.set (index, element);
	}

	@Override
	public int size () {
		if (values == null) {
			return 0;
		}
		return values.size ();
	}

	@Override
	public List<Object> subList (int fromIndex, int toIndex) {
		if (values == null) {
			return null;
		}
		return values.subList (fromIndex, toIndex);
	}

	@Override
	public Object [] toArray () {
		if (values == null) {
			return null;
		}
		return values.toArray ();
	}

	@Override
	public <T> T [] toArray (T [] a) {
		if (values == null) {
			return null;
		}
		return values.toArray (a);
	}

}