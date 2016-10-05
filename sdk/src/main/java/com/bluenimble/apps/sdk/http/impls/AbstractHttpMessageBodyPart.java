package com.bluenimble.apps.sdk.http.impls;

import com.bluenimble.apps.sdk.http.HttpMessageBodyPart;

public abstract class AbstractHttpMessageBodyPart implements HttpMessageBodyPart {
	
	private static final long serialVersionUID = 5209163648562691869L;

	protected String 		name;

	protected String 		contentType;
	protected String 		transferEncoding;

	public AbstractHttpMessageBodyPart () {
	}
	
	public AbstractHttpMessageBodyPart (String name) {
		this.name = name;
	}

	public void setContentType (String contentType) {
		this.contentType = contentType;
	}

	@Override
	public String getContentType () {
		return contentType;
	}

	public void setTransferEncoding (String transferEncoding) {
		this.transferEncoding = transferEncoding;
	}

	@Override
	public String getTransferEncoding () {
		return transferEncoding;
	}

	public void setName (String name) {
		this.name = name;
	}
	
	@Override
	public String getName () {
		return name;
	}

}
