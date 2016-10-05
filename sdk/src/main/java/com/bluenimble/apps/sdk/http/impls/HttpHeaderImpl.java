package com.bluenimble.apps.sdk.http.impls;

import java.util.ArrayList;
import java.util.List;

import com.bluenimble.apps.sdk.http.HttpHeader;

public class HttpHeaderImpl implements HttpHeader {
	
	private static final long serialVersionUID = -2340728072298016526L;
	
	private static final String [] EMPTY_ARRAY = new String [] { };
	
	protected String name;
	protected List<String> values;
	
	public HttpHeaderImpl (String name, String value) {
		this.name = name;
		if (value != null) {
			this.values = new ArrayList<String> ();
			this.values.add (value);
		}
	}
	
	public HttpHeaderImpl (String name, List<String> values) {
		this.name = name;
		this.values = values;
	}
	
	@Override
	public String getName () {
		return name;
	}
	
	public void setName (String name) {
		this.name = name;
	}
	
	@Override
	public String [] getValues () {
		if (values == null || values.isEmpty ()) {
			return EMPTY_ARRAY;
		} 
		return values.toArray (new String [values.size ()]);
	}
	
	@Override
	public String toString () {
		StringBuilder sb = new StringBuilder ();
		if (name != null) {
			sb.append ("[").append (name).append ("]").append ("=>");
		}
		if (values != null && !values.isEmpty ()) {
			for (int i = 0; i < values.size (); i++) {
				Object v  = values.get (i);
				sb.append (v);
				if (i < (values.size () - 1)) {
					sb.append ("|");
				}
			}
		}
		String s = sb.toString ();
		sb.setLength (0);
		sb = null;
		return s;
	}
	
}
