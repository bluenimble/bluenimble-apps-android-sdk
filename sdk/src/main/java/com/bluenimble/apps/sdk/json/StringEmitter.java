package com.bluenimble.apps.sdk.json;

public class StringEmitter extends AbstractEmitter {
	
	private StringBuilder 	buff;
	
	public StringEmitter (StringBuilder buff) {
		this.buff = buff;
	}

	@Override
	public JsonEmitter write (String text) {
		buff.append (text);
		return this;
	} 
	
}
