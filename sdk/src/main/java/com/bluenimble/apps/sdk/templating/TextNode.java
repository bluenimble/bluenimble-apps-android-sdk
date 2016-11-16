package com.bluenimble.apps.sdk.templating;

public class TextNode implements Node {

	private static final long serialVersionUID = 5233545719136265499L;
	
	private String text;
	
	public TextNode (String text) {
		this.text = text;
	}

	@Override
	public String token () {
		return text;
	}
	
}
