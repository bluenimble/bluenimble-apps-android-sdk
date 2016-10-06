package com.bluenimble.apps.sdk.ui.themes;

import android.graphics.Typeface;

import com.bluenimble.apps.sdk.spec.ThemeSpec;

import java.io.InputStream;
import java.io.Serializable;

/**
 * Created by LINVI on 06/10/2016.
 */

public interface FontsRegistry extends Serializable {

    Typeface    lookup		(String id);

    void 	    register	(String id, Typeface typeface) throws Exception;

}
