package com.bluenimble.apps.sdk.spec;

import com.bluenimble.apps.sdk.Lang;

import java.io.Serializable;

public class ViewSize implements Serializable {

	private float width;
	private float height;

	public ViewSize (float width, float height) {
		this.width = width;
		this.height = height;
	}

	public float getWidth () {
		return this.width;
	}

	public float getHeight () {
		return this.height;
	}

	public String toString () {
		return Lang.ARRAY_OPEN + width + Lang.COMMA + height + Lang.ARRAY_CLOSE;
	}

}
