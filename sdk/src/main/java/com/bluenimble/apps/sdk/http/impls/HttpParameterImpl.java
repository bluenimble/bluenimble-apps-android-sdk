package com.bluenimble.apps.sdk.http.impls;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import com.bluenimble.apps.sdk.http.HttpParameter;
import com.bluenimble.apps.sdk.http.utils.HttpUtils;

public class HttpParameterImpl implements HttpParameter {
	
	private static final long serialVersionUID = -2340728072298016526L;
	
	protected String name;
	protected Object value;
	
	public HttpParameterImpl (String name, Object value) {
		this.name = name;
		this.value = value;
	}
	
	@Override
	public String getName () {
		return name;
	}
	
	public void setName (String name) {
		this.name = name;
	}
	
	@Override
	public Object getValue () {
		return value;
	}
	
	public void setValue (Object value) {
		this.value = value;
	}
	
	@Override
	public String dump (StringBuilder sb, String charset) throws UnsupportedEncodingException {
		if (charset == null) {
			charset = HttpUtils.DEFAULT_ENCODING;
		}
		boolean returnResult = false;
		if (sb == null) {
			returnResult = true;
			sb = new StringBuilder ();
		}
		if (name != null) {
			sb.append (URLEncoder.encode (name, charset));
		}
		if (value != null) {
			sb.append ("=");
			sb.append (URLEncoder.encode (value.toString (), charset));
		}
		if (returnResult) {
			String s = sb.toString ();
			sb.setLength (0);
			sb = null;
			return s;
		}
		return null;
	}
	
	@Override
	public String toString () {
		StringBuilder sb = new StringBuilder ();
		try {
			dump (sb, HttpUtils.DEFAULT_ENCODING);
		} catch (UnsupportedEncodingException e) {
			// IGNORE
		}
		String s = sb.toString ();
		sb.setLength (0);
		sb = null;
		return s;
	}
	
}
