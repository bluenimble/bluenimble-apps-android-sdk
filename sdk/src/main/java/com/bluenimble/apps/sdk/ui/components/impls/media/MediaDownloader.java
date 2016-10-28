package com.bluenimble.apps.sdk.ui.components.impls.media;

import android.net.Uri;

import com.bluenimble.apps.sdk.Lang;
import com.bluenimble.apps.sdk.controller.DataHolder;
import com.bluenimble.apps.sdk.controller.StreamSource;
import com.bluenimble.apps.sdk.controller.impls.data.DefaultDataHolder;
import com.bluenimble.apps.sdk.spec.ApplicationSpec;
import com.bluenimble.apps.sdk.utils.BackendHelper;
import com.squareup.picasso.Downloader;

import java.io.IOException;

public class MediaDownloader implements Downloader {

	public static Downloader Instance = null;

	private static final String ProtocolToken = "//:";

	private ApplicationSpec application;

	public MediaDownloader (ApplicationSpec application) {
		this.application = application;
	}

	public static void create (ApplicationSpec application) {
		if (Instance != null) {
			return;
		}
		Instance = new MediaDownloader (application);
	}

	@Override
	public Response load (Uri uri, int networkPolicy) throws IOException {

		String sUri = uri.toString ();

		// service:// serviceId ? a=aValue & b=bValue & c=cValue
		String spec 		= sUri.substring (sUri.indexOf (ProtocolToken) + ProtocolToken.length ());

		String 		serviceId 	= spec;
		String [] 	params 		= null;

		int indexOfQM = spec.indexOf (Lang.QMARK);
		if (indexOfQM > 0) {
			serviceId 	= spec.substring (0, indexOfQM);
			params 		= Lang.split (spec.substring (indexOfQM), Lang.AMP, true);
		}

		DataHolder dh = new DefaultDataHolder ();
		if (params != null && params.length > 0) {
			for (String p : params) {
				String pn = p;
				String pv = null;
				int indexOfEq = p.indexOf (Lang.EQUALS);
				if (indexOfEq > 0) {
					pn = p.substring (0, indexOfEq);
					pv = p.substring (indexOfEq + 1);
				}
				if (Lang.isNullOrEmpty (pv)) {
					dh.set (DataHolder.Namespace.View, pv, Lang.split (pn, Lang.DOT, true));
				}
			}
		}

		try {
			BackendHelper.callService (serviceId, dh, application);
		} catch (Exception e) {
			throw new IOException (e.getMessage (), e);
		}

		StreamSource ss = dh.stream (serviceId + Lang.DOT + DataHolder.Namespace.Streams + Lang.DOT + DataHolder.DefaultStreamId);

		return new Response (ss.stream (), true, ss.length ());
	}

	@Override
	public void shutdown () {

	}

}
