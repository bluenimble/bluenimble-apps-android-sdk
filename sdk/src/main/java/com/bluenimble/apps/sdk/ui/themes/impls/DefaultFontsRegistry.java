package com.bluenimble.apps.sdk.ui.themes.impls;

import android.graphics.Typeface;

import com.bluenimble.apps.sdk.ui.themes.FontsRegistry;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by LINVI on 06/10/2016.
 */

public class DefaultFontsRegistry implements FontsRegistry {

    private Map<String, Typeface> fonts;

    @Override
    public void register (String id, Typeface typeface) throws Exception {
        if (fonts == null) {
            fonts = new HashMap<String, Typeface>();
        }
        fonts.put (id, typeface);
    }

    @Override
    public Typeface lookup (String id) {
        if (fonts == null) {
            return null;
        }
        return fonts.get (id);
    }
}
