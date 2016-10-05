package com.bluenimble.apps.sdk.spec;

import java.io.Serializable;

public interface SdkVersion extends Serializable {

	int major();
	int minor();
	int patch();
	
}
