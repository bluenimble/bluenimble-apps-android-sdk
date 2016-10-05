package com.bluenimble.apps.sdk;

import java.io.Serializable;

public final class Null implements Serializable {

	private static final long serialVersionUID = -4074799016571685758L;
	
	public static final Null Instance = new Null ();

    @Override
	protected final Object clone() {
        return this;
    }

    @Override
	public boolean equals(Object object) {
        return object == null || object == this;
    }

    @Override
	public String toString () {
        return "!NULL!";
    }
    
}


