package com.bluenimble.apps.sdk.http.impls;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import com.bluenimble.apps.sdk.http.HttpMessageBody;
import com.bluenimble.apps.sdk.http.HttpMessageBodyPart;

public class HttpMessageBodyImpl implements HttpMessageBody {
	
	private static final long serialVersionUID = -4689114412779498380L;

	protected List<HttpMessageBodyPart> parts;
	
	public HttpMessageBodyImpl () {
	}
	public HttpMessageBodyImpl (HttpMessageBodyPart content) {
		add (content);
	}
	
	@Override
	public void dump (OutputStream output, String charset, String boundary) throws IOException {
		
		OutputStreamWriter osw = null;
		
		if (charset == null) {
			osw = new OutputStreamWriter (output);
		} else {
			osw = new OutputStreamWriter (output, charset);
		}
		
		Writer writer = new PrintWriter (osw, true);
		
		for (int i = 0; i < count (); i++) {
			
			HttpMessageBodyPart part = parts.get (i);
			
			if (boundary != null) {
				writer.append ("--" + boundary).append (CRLF);
				
				String name = part.getName ();
				if (name == null) {
					name = "Part-" + i;
				}
				
				String fname = part.getFileName ();
				if (fname == null) {
					fname = name;
				}
				
			    writer.append ("Content-Disposition: form-data; name=\"" + name + "\"; filename=\"" + fname + "\"").append (CRLF);
			    if (part.getContentType () != null) {
			    	writer.append ("Content-Type: " + part.getContentType ()).append (CRLF);
				}
			    if (part.getTransferEncoding () != null) {
			    	writer.append ("Content-Transfer-Encoding: " + part.getTransferEncoding ()).append (CRLF);
				}
			    
			    writer.append (CRLF).flush ();
			}
			
			part.dump (output, charset);
			
			if (boundary != null) {
				writer.append (CRLF).flush (); 
			}
			
		}
		if (boundary != null) {
		    writer.append ("--" + boundary + "--").append (HttpMessageBody.CRLF).flush ();
		}
	}

	@Override
	public void add (HttpMessageBodyPart part) {
		if (parts == null) {
			parts = new ArrayList<HttpMessageBodyPart> ();
		}
		parts.add (part);
	}
	
	@Override
	public HttpMessageBodyPart get (int index) {
		if (parts == null) {
			return null;
		}
		return parts.get (index);
	}
	@Override
	public int count () {
		if (parts == null) {
			return 0;
		}
		return parts.size ();
	}
	
}
