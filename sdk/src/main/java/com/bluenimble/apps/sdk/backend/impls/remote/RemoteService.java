package com.bluenimble.apps.sdk.backend.impls.remote;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.bluenimble.apps.sdk.IOUtils;
import com.bluenimble.apps.sdk.Json;
import com.bluenimble.apps.sdk.Lang;
import com.bluenimble.apps.sdk.spec.ApplicationSpec;
import com.bluenimble.apps.sdk.templating.VariableResolver;
import com.bluenimble.apps.sdk.backend.Service;
import com.bluenimble.apps.sdk.controller.DataHolder;
import com.bluenimble.apps.sdk.controller.StreamSource;
import com.bluenimble.apps.sdk.json.JsonObject;

import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class RemoteService implements Service {

	private static final long serialVersionUID = 2077148821012512850L;

	protected OkHttpClient HttpClient = new OkHttpClient ();

	public interface Spec {
		String Verb 		= "verb";
		String Url 			= "url";
		String Data 		= "data";
		String Headers 		= "headers";
		interface proxy {
			String Type 	= "type";
			String Endpoint = "endpoint";
			String Port 	= "port";
			interface auth 	{
				String user 	= "user";
				String password = "password";
			}
		}
		interface timeouts {
			String Connect 	= "connect";
			String Read 	= "read";
		}
		interface response {
			String Id 	= "id";
		}
	}

	enum Verb {
		GET,
		POST,
		PUT,
		PATCH,
		DELETE
	}

	interface ContentTypes {
		String FormUrlEncoded		= "application/x-www-form-urlencoded";

		String Multipart 			= "multipart/form-data";

		String Text 				= "text/plain";
		String Html 				= "text/html";
		String Json 				= "application/json";
		String Xml          		= "application/xml";

		String Stream				= "application/octet-stream";

	}

	protected Map<String, MediaType> 	MediaTypes 	= new HashMap<String, MediaType> ();

	public RemoteService () {
		// media types
		MediaTypes.put (ContentTypes.Json, MediaType.parse (ContentTypes.Json));
		MediaTypes.put (ContentTypes.FormUrlEncoded, MediaType.parse (ContentTypes.FormUrlEncoded));
		MediaTypes.put (ContentTypes.Multipart, MediaType.parse (ContentTypes.Multipart));
		MediaTypes.put (ContentTypes.Text, MediaType.parse (ContentTypes.Text));
	}

	@Override
	public void execute (final String id, JsonObject masterSpec, ApplicationSpec application, DataHolder dh) throws Exception {

		application.logger ().info (RemoteService.class.getSimpleName () + " ---> Execute", "Service " + id);

		if (masterSpec == null) {
			throw new Exception ("backend service spec not found");
		}

		application.logger ().info (RemoteService.class.getSimpleName () + " ---> Execute", "Master Spec : " + masterSpec.toString ());

		final JsonObject spec = masterSpec.duplicate ();
		
		// visit url
		resolve (spec, Spec.Url, application, dh);

		application.logger ().info (RemoteService.class.getSimpleName () + " ---> Execute", "Resolved Master Spec : " + spec.toString ());

		// verb
		Verb verb = null;
		try {
			verb = Verb.valueOf (Json.getString (spec, Spec.Verb, Verb.GET.name ()).toUpperCase ());
		} catch (Exception ex) {
			verb = Verb.GET;
		}

		// url
		String url = Json.getString (spec, Spec.Url);

		// contentType
		String contentType = ContentTypes.FormUrlEncoded;

		// resole and add headers
		JsonObject headers = Json.getObject (spec, Spec.Headers);
		if (!Json.isNullOrEmpty (headers)) {
			Json.resolve (headers, application.expressionCompiler (), dh);
			Iterator<String> hnames = headers.keys ();
			while (hnames.hasNext ()) {
				String hn = hnames.next ();
				String hv = Json.getString (headers, hn);
				if (HttpHeaders.CONTENT_TYPE.toUpperCase ().equals (hn.toUpperCase ())) {
					contentType = hv;
				}
			}
		}

		String accept = Json.getString (headers, HttpHeaders.ACCEPT);

		// resolve and add data - if no data section defined at the service level, it should be default to the View ns
		JsonObject rdata = Json.getObject (spec, Spec.Data);
		if (Json.isNullOrEmpty (rdata)) {
			spec.set (Spec.Data, dh.get (DataHolder.Namespace.View));
		} else {
			Json.resolve (rdata, application.expressionCompiler (), dh);
		}

		if (Lang.isNullOrEmpty (contentType)) {
			contentType = ContentTypes.FormUrlEncoded;
		}

		contentType = contentType.trim ();

		MediaType mediaType = MediaTypes.get (contentType);

		RequestBody body = null;

		if (contentType.startsWith (ContentTypes.Json)) {
			body = RequestBody.create (mediaType, rdata.toString ());
		} else if (contentType.startsWith (ContentTypes.Multipart)) {
			// multipart body

		} else {
			if (!Json.isNullOrEmpty (rdata)) {
				if (verb.equals (Verb.POST) || verb.equals (Verb.PUT) || verb.equals (Verb.PATCH)) {
					FormBody.Builder fb = new FormBody.Builder ();

					Iterator<String> pnames = rdata.keys ();
					while (pnames.hasNext ()) {
						String pn = pnames.next ();
						fb.add (pn, String.valueOf (rdata.get (pn)));
					}

					body = fb.build ();
				} else if (verb.equals (Verb.GET)) {
					HttpUrl.Builder urlBuilder = HttpUrl.parse("https://api.github.help").newBuilder ();
					Iterator<String> pnames = rdata.keys ();
					while (pnames.hasNext ()) {
						String pn = pnames.next ();
						urlBuilder.addQueryParameter (pn, String.valueOf (rdata.get (pn)));
					}
					url = urlBuilder.build ().toString ();
				}
			}
		}

		// create the request builder
		Request.Builder rBuilder = new Request.Builder ().url (url);
		// add headers
		if (!Json.isNullOrEmpty (headers)) {
			Iterator<String> hnames = headers.keys ();
			while (hnames.hasNext ()) {
				String hn = hnames.next ();
				String hv = Json.getString (headers, hn);
				rBuilder.header (hn, hv);
			}
		}
		// add body if any
		if (body != null) {
			if (verb.equals (Verb.POST)) {
				rBuilder.post (body);
			} else if (verb.equals (Verb.PUT)) {
				rBuilder.put (body);
			} else if (verb.equals (Verb.PUT)) {
				rBuilder.patch (body);
			} else if (verb.equals (Verb.DELETE)) {
				rBuilder.delete ();
			} else if (verb.equals (Verb.GET)) {
				rBuilder.get ();
			}
		}

		Request request = rBuilder.build ();
		application.logger ().error (RemoteService.class.getSimpleName () + " ---> Execute", "Request : " + request.toString ());
		final Response response;
		//TODO : to remove try / catch
		try {
			response = HttpClient.newCall (request).execute ();
		} catch (IOException e) {
			e.printStackTrace ();
			throw new Exception (e);
		}
		application.logger ().error (RemoteService.class.getSimpleName () + " ---> Execute", "Response: " + response.toString ());
		
		if (response.isSuccessful ()) {
			final ResponseBody rBody = response.body ();
			if (rBody == null || rBody.contentLength () == 0) {
				return;
			}
			String rContentType = response.header (HttpHeaders.CONTENT_TYPE);
			if (ContentTypes.Json.equals (accept) || (rContentType != null && rContentType.startsWith (ContentTypes.Json))) {
				dh.set (id, new JsonObject (rBody.string ()));
			} else if (ContentTypes.Text.equals (accept) || (rContentType != null && rContentType.startsWith (ContentTypes.Text))) {
				dh.set (id, rBody.string ());
			} else if (ContentTypes.Html.equals (accept) || (rContentType != null && rContentType.startsWith (ContentTypes.Html))) {
				dh.set (id, rBody.string ());
			} else {
				dh.stream (new StreamSource () {
					private InputStream stream;
					@Override
					public String id () {
						return id + Lang.DOT + DataHolder.Namespace.Streams + Json.getString (spec, Spec.response.Id, DataHolder.DefaultStreamId);
					}
					@Override
					public String name () {
						String disposition = response.header (HttpHeaders.CONTENT_DISPOSITION);
						if (disposition == null) {
							return null;
						}
						return disposition.replaceFirst ("(?i)^.*filename=\"([^\"]+)\".*$", "$1");
					}
					@Override
					public String contentType () {
						return response.header (HttpHeaders.CONTENT_TYPE);
					}
					@Override
					public long length () {
						String sLength = response.header (HttpHeaders.CONTENT_LENGTH);
						if (Lang.isNullOrEmpty (sLength)) {
							return 0;
						}
						return Long.valueOf (sLength);
					}
					@Override
					public InputStream stream () {
						if (stream != null) {
							return stream;
						}
						stream = rBody.byteStream ();
						return stream;
					}
					@Override
					public void close () {
						if (stream == null) {
							return;
						}
						IOUtils.closeQuietly (stream);
					}
				});
			}
		}
	}
	
	private void resolve (JsonObject o, String key, ApplicationSpec application, VariableResolver vr) {
		o.set (key, application.expressionCompiler ().compile (String.valueOf (o.get (key)), null).eval (vr));
	}
	
}
