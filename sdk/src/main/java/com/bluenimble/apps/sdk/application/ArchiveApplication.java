package com.bluenimble.apps.sdk.application;

import com.bluenimble.apps.sdk.spec.impls.json.ArchiveApplicationSpec;
import com.bluenimble.apps.sdk.spec.impls.json.DiskApplicationSpec;

public class ArchiveApplication extends DiskApplication {

    @Override
    protected void reload () throws Exception {
        spec = new ArchiveApplicationSpec (this);
        setLogLevel ();
    }

}
