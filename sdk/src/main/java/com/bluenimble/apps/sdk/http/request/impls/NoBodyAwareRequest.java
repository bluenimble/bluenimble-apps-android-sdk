package com.bluenimble.apps.sdk.http.request.impls;

import java.net.HttpURLConnection;
import java.util.ArrayList;

import com.bluenimble.apps.sdk.Lang;
import com.bluenimble.apps.sdk.http.HttpEndpoint;
import com.bluenimble.apps.sdk.http.HttpParameter;
import com.bluenimble.apps.sdk.http.request.HttpRequestWriteException;
import com.bluenimble.apps.sdk.http.utils.HttpUtils;

public abstract class NoBodyAwareRequest extends AbstractHttpRequest {

	private static final long serialVersionUID = 6156567388966926923L;

	protected NoBodyAwareRequest (String method, HttpEndpoint endpoint) {
		super (method, endpoint);
		if (!Lang.isNullOrEmpty (endpoint.getQuery ())) {
			parameters = new ArrayList<HttpParameter> ();
			HttpUtils.parseParameters (endpoint.getQuery (), parameters);
			setParameters (parameters);
			endpoint.setQuery (null);
		}
	}
	
	@Override
	public void write (HttpURLConnection hc) throws HttpRequestWriteException {
		if (visitor != null) {
			visitor.visit (this, hc);
		}
		addHeaders (hc);
		// nothing going to the body
	}
	
	@Override
	public String toString () {
		StringBuilder sb = new StringBuilder ();
		try {
			sb.append (name).append (" ").append (getURI ()).append ("\n");
		} catch (Exception e) {
			
		}
		sb.append (super.toString ());
		String s = sb.toString ();
		sb.setLength (0);
		sb = null;
		return s;
	}
	
}
