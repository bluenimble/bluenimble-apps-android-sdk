package com.bluenimble.apps.sdk.http.request.impls;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import com.bluenimble.apps.sdk.Lang;
import com.bluenimble.apps.sdk.http.HttpEndpoint;
import com.bluenimble.apps.sdk.http.HttpHeader;
import com.bluenimble.apps.sdk.http.HttpHeaders;
import com.bluenimble.apps.sdk.http.HttpMessageBody;
import com.bluenimble.apps.sdk.http.HttpParameter;
import com.bluenimble.apps.sdk.http.impls.HttpHeaderImpl;
import com.bluenimble.apps.sdk.http.request.HttpRequestWriteException;
import com.bluenimble.apps.sdk.http.utils.ContentTypes;
import com.bluenimble.apps.sdk.http.utils.HttpUtils;

public class BodyAwareRequest extends AbstractHttpRequest {

	private static final long serialVersionUID = -3367718889542333065L;
	
	protected BodyAwareRequest (String name, HttpEndpoint endpoint) {
		super (name, endpoint);
		contentType = ContentTypes.FormUrlEncoded;
		if (!Lang.isNullOrEmpty (endpoint.getQuery ())) {
			parameters = new ArrayList<HttpParameter> ();
			HttpUtils.parseParameters (endpoint.getQuery (), parameters);
			setParameters (parameters);
			endpoint.setQuery (null);
		}
	}

	@Override
	public void write (HttpURLConnection hc) throws HttpRequestWriteException {
		
		hc.setUseCaches (isCachingEnabled ());

		String boundary = setContentType ();
		
		if (visitor != null) {
			visitor.visit (this, hc);
		}
		
		addHeaders (hc);
		
		Writer writer = null;
		try {
			
			OutputStream os = null;
			
			if (isDebugMode ()) {
				os = System.out;
			} else {
				os = hc.getOutputStream ();
			}

			OutputStreamWriter osw = 
					(charset == null ? new OutputStreamWriter (os) : 
									   new OutputStreamWriter (os, charset));
			writer = new PrintWriter (osw, true);
			
			if (hasParameters () && getBodyPartsCount () == 0) {
				writer.append (super.dumpParameters ()).flush ();
				return;
			}
			if (getBodyPartsCount () > 0) {
				if (hasParameters ()) {
					String cs = charset != null ? charset : HttpUtils.DEFAULT_ENCODING;
					for (HttpParameter p : getParameters ()) {
						writer.append ("--" + boundary).append (HttpMessageBody.CRLF);
					    writer.append ("Content-Disposition: form-data; name=\"" + p.getName () + "\"").append (HttpMessageBody.CRLF);
					    writer.append ("Content-Type: text/plain; charset=" + cs).append (HttpMessageBody.CRLF);
					    writer.append (HttpMessageBody.CRLF);
					    writer.append (String.valueOf (p.getValue ())).append (HttpMessageBody.CRLF).flush ();
					}
				} 
				body.dump (os, charset, boundary);
				return;
			}
			
		} catch (Throwable th) {
			throw new HttpRequestWriteException (th);
		} finally {
			if (writer != null) {
				try {
					writer.close ();
				} catch (IOException e) {
					// IGNORE
				}
			}
		} 

	}
	
	private String setContentType () {
		String boundary = getBoundary ();

		String ct = contentType;
		
		if (boundary != null) {
			ct = contentType + "; boundary=" + boundary;
		} 
		List<HttpHeader> headers = getHeaders ();
		if (headers == null) {
			headers = new ArrayList<HttpHeader> ();
			setHeaders (headers);
		}
		headers.add (new HttpHeaderImpl (HttpHeaders.CONTENT_TYPE, ct));
		return boundary;
	}

	/**
	 * return boundary 	if hasParameters and at least body
	 * 					if more than one body
	 * @return
	 */
	private String getBoundary () {
		if ((getBodyPartsCount () == 1 && (ContentTypes.Multipart.equals (contentType)) || hasParameters ()) 
				|| getBodyPartsCount () > 1) {
			return Long.toHexString (System.currentTimeMillis ());
		} 
		return null;
	}
	
	private int getBodyPartsCount () {
		if (body == null) {
			return 0;
		}
		return body.count ();
	}
	
	@Override
	protected String dumpParameters () throws UnsupportedEncodingException {
		return null;
	}
	
	@Override
	public URI getURI () throws UnsupportedEncodingException, URISyntaxException {
		return HttpUtils.createURI (endpoint, null);
	}

	@Override
	public String toString () {
		StringBuilder sb = new StringBuilder ();
		try {
			sb.append (name).append (" ").append (getURI ()).append ("\n");
			String ps = super.dumpParameters ();
			if (ps != null) {
				sb.append (ps).append ("\n");
			} else {
				sb.append ("<NO PARAMS>");
			}
			sb.append ("\n");
		} catch (Exception e) {
			
		}
		sb.append (super.toString ());
		
		if (getBodyPartsCount () > 0) {
			sb.append ("\n").append ("<BODY PARTS>").append (getBodyPartsCount ());
		}
		
		String s = sb.toString ();
		sb.setLength (0);
		sb = null;
		return s;
	}

}
