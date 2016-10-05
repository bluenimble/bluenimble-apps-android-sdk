package com.bluenimble.apps.sdk.http.impls;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.bluenimble.apps.sdk.IOUtils;

public class InputStreamHttpMessageBodyPart extends AbstractHttpMessageBodyPart {
	
	private static final long serialVersionUID = 5209163648562691869L;

	private static final int DEFAULT_BUFFER_SIZE = 1024 * 4;

	protected InputStream 	content;
	protected String 		fileName;

	public InputStreamHttpMessageBodyPart (InputStream content) {
		this.content = content;
	}
	
	public InputStreamHttpMessageBodyPart (String name, String fileName, InputStream content) {
		super (name);
		this.fileName = fileName;
		this.content = content;
	}
	
	@Override
	public void dump (OutputStream output, String charset) throws IOException {
		if (content == null) {
			return;
		}
		try {
			byte [] buffer = new byte [DEFAULT_BUFFER_SIZE];
			int n = 0;
			while (-1 != (n = content.read (buffer))) {
				output.write (buffer, 0, n);
			}
			output.flush ();
		} finally {
			IOUtils.closeQuietly (content);
		}
	}

	@Override
	public void close () {
		if (content == null) {
			return;
		}
		try {
			content.close ();
		} catch (IOException ioex) {
			// IGNORE
		}
	}
	
	@Override
	public InputStream toInputStream () {
		return content;
	}

	@Override
	public String getFileName () {
		if (fileName != null) {
			return fileName;
		}
		return getName ();
	}

}
