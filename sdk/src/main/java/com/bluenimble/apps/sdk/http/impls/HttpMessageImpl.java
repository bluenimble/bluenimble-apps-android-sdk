package com.bluenimble.apps.sdk.http.impls;

import java.util.List;

import com.bluenimble.apps.sdk.http.HttpHeader;
import com.bluenimble.apps.sdk.http.HttpMessage;
import com.bluenimble.apps.sdk.http.HttpMessageBody;
import com.bluenimble.apps.sdk.http.utils.HttpUtils;

public class HttpMessageImpl implements HttpMessage {

	private static final long serialVersionUID = 2488082619025875514L;

	protected String charset = HttpUtils.DEFAULT_ENCODING;
	
	protected String contentType;
	
	protected List<HttpHeader> headers;
	protected HttpMessageBody body;
	
	@Override
	public String getCharset () {
		return charset;
	}
	
	public void setCharset (String charset) {
		this.charset = charset;
	}
	
	@Override
	public String getContentType () {
		return contentType;
	}
	
	public void setContentType (String contentType) {
		this.contentType = contentType;
	}
	
	@Override
	public List<HttpHeader> getHeaders () {
		return headers;
	}
	
	public void setHeaders (List<HttpHeader> headers) {
		this.headers = headers;
	}
	
	@Override
	public HttpHeader getHeader (String name) {
		if (name == null || headers == null || headers.isEmpty ()) {
			return null;
		}
		for (HttpHeader h : headers) {
			if (name.equals (h.getName ())) {
				return h;
			}
		}
		return null;
	}

	@Override
	public HttpMessageBody getBody () {
		return body;
	}
	
	public void setBody (HttpMessageBody body) {
		this.body = body;
	}
	
	@Override
	public String toString () {
		StringBuilder sb = new StringBuilder ();
		if (headers != null && !headers.isEmpty ()) {
			sb.append ("<HEADERS>");
			for (HttpHeader h : headers) {
				sb.append ("\n").append ("\t").append (h);
			}
		}
		String s = sb.toString ();
		sb.setLength (0);
		sb = null;
		return s;
	}

}
