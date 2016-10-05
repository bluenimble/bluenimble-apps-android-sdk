package com.bluenimble.apps.sdk.http.impls;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class StringHttpMessageBodyPart extends AbstractHttpMessageBodyPart {
	
	private static final long serialVersionUID = 5209163648562691869L;

	protected String content;

	public StringHttpMessageBodyPart (String content) {
		this.content = content;
	}
	
	public StringHttpMessageBodyPart (String name, String content) {
		super (name);
		this.content = content;
	}
	
	@Override
	public void dump (OutputStream output, String charset) throws IOException {
		output.write (charset == null ? content.getBytes () : content.getBytes (charset));
		output.flush ();
	}

	@Override
	public void close () {
	}
	
	@Override
	public InputStream toInputStream () {
		if (content == null) {
			return null;
		}
		return new ByteArrayInputStream (content.getBytes ());
	}

	@Override
	public String getFileName () {
		return getName ();
	}

}
