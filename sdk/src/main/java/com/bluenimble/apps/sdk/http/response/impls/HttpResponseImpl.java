package com.bluenimble.apps.sdk.http.response.impls;

import com.bluenimble.apps.sdk.http.impls.HttpMessageImpl;
import com.bluenimble.apps.sdk.http.response.HttpResponse;

public class HttpResponseImpl extends HttpMessageImpl implements HttpResponse {

	private static final long serialVersionUID = 9027275355235686148L;
	
	protected String id;
	protected int status;
	
	public HttpResponseImpl (String id) {
		this.id = id;
	}
	
	@Override
	public String getId () {
		return id;
	}

	@Override
	public int getStatus () {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
	
	@Override
	public String toString () {
		StringBuilder sb = new StringBuilder ();

		sb.append ("<STATUS>").append (" ").append (String.valueOf (status)).append ("\n");
		
		sb.append (super.toString ());
		
		sb.append ("\n").append ("\n").append ("<BODY> ");
		if (getBody () != null) {
			sb.append ("#PARTS ").append (getBody ().count ()).append ("\n");
		}
		
		String s = sb.toString ();
		sb.setLength (0);
		sb = null;
		return s;
	}

}
