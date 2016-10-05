package com.bluenimble.apps.sdk.http.impls;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.zip.GZIPInputStream;

import com.bluenimble.apps.sdk.Lang;
import com.bluenimble.apps.sdk.http.HttpClient;
import com.bluenimble.apps.sdk.http.HttpClientException;
import com.bluenimble.apps.sdk.http.HttpHeader;
import com.bluenimble.apps.sdk.http.HttpHeaders;
import com.bluenimble.apps.sdk.http.HttpMethods;
import com.bluenimble.apps.sdk.http.request.HttpRequest;
import com.bluenimble.apps.sdk.http.response.HttpResponse;
import com.bluenimble.apps.sdk.http.response.impls.HttpResponseImpl;

public class DefaultHttpClient implements HttpClient {

	private static final long serialVersionUID = 8918611377786119408L;
	
	private static final String GZip = "gzip";
	
	static {
		System.setProperty ("http.agent", "");
	} 
	
	protected String [] cookies;
	
	protected boolean updateCookies;
	
	@Override
	public HttpResponse send (final HttpRequest request) throws HttpClientException {

		HttpURLConnection hc = null;
		
		try {
			if (request == null || request.getURI () == null) {
				throw new HttpClientException("No request to proceed");
			}

			URL url = request.getURI ().toURL ();
			
			URLConnection connection = null;
			if (request.getProxy () != null) {
				connection = url.openConnection (request.getProxy ());
			} else {
				connection = url.openConnection ();
			}
			
			hc = (HttpURLConnection) connection;
			
			/*
			if (hc instanceof HttpsURLConnection) {
				
				System.out.println ("Its HttpsURLConnection");
				
				System.out.println ("Snis: " + request.getSniHosts ());
				
				HttpsURLConnection https = (HttpsURLConnection)hc;
				
				if (ssf != null) {
					https.setSSLSocketFactory (ssf);
				} else {
					if (request.getSniHosts () != null) {
						System.out.println ("Have snis");
						List<SNIServerName> serverNames = new ArrayList<SNIServerName> ();
						for (String sniHhost : request.getSniHosts ()) {
							SNIHostName serverName = new SNIHostName (new URL (sniHhost).getHost ());
							serverNames.add (serverName);
						}

						SSLParameters params = new SSLParameters ();
						params.setServerNames (serverNames);
						
						https.setSSLSocketFactory (new SSLSocketFactoryWrapper (HttpsURLConnection.getDefaultSSLSocketFactory (), params));								
					}
				}
				
				if (trustAll) {
					https.setHostnameVerifier (new HostnameVerifier () {
						@Override
						public boolean verify (String host, SSLSession session) {
							if (!host.equalsIgnoreCase (session.getPeerHost ())) {
			                    System.out.println ("Warning: URL host '" + host + "' is different than SSLSession host '" + session.getPeerHost () + "'.");
			                }
							return true;
						}
					});
				}
				
			}
			*/
			hc.setConnectTimeout 	(request.getConnectTimeout ());
			hc.setReadTimeout 		(request.getReadTimeout ());
			
			hc.setRequestMethod (request.getName ());

			if (request.getName ().equals (HttpMethods.POST) || request.getName ().equals (HttpMethods.PUT)) {
				connection.setDoOutput (true);
			}

			if (!(connection instanceof HttpURLConnection)) {
				throw new HttpClientException ("Only Http request can be handled");
			}
			
			setRequestCookie (request);
			
			request.write (hc);
			
			InputStream iobody = null;
			
			int status = hc.getResponseCode ();
			
			HttpResponseImpl response = new HttpResponseImpl (request.getId ());
			response.setStatus (status);

			addResponseHeaders (response, hc);
			
			String charset = request.getCharset ();
			
			HttpHeader cth = response.getHeader (HttpHeaders.CONTENT_TYPE);
			if (cth != null) {
				String [] values = cth.getValues ();
				if (values != null && values.length > 0) {
					String contentType = values [0];
					response.setContentType (contentType);
					for (String param : contentType.replace (" ", "").split(";")) {
					    if (param.startsWith ("charset=")) {
					        charset = param.split("=", 2)[1];
					        break;
					    }
					}
				} 
			}
			response.setCharset (charset);
			
			if (request.getSuccessCodes ().contains (String.valueOf (status))) {
				iobody = hc.getInputStream ();
			} else {
				iobody = hc.getErrorStream ();
			}
			
			if (GZip.equals (hc.getContentEncoding ())) {
				iobody = new GZIPInputStream (iobody);
			} 
			
			response.setBody (new HttpMessageBodyImpl (new InputStreamHttpMessageBodyPart (iobody)));
			
			updateCookies (response);

			return response;
			
		} catch (Throwable th) {
			throw new HttpClientException (th);
		} 

	}
	
	protected void onCreateConnection (HttpRequest request, HttpURLConnection hc) {
		
	}

	protected void setRequestCookie (HttpRequest request) {
		if (cookies == null) {
			return;
		}
		if (request.getHeaders () == null) {
			request.setHeaders (new ArrayList<HttpHeader> ());
		}
		List<String> cookieValues = new ArrayList<String> ();
		for (String cookie : cookies) {
			cookieValues.add (cookie.split (Lang.SEMICOLON, 2)[0]);
		}
		HttpHeader cookieHeader = new HttpHeaderImpl (HttpHeaders.COOKIE, cookieValues);
		request.getHeaders ().add (cookieHeader);
	}
	
	protected void updateCookies (HttpResponse response) {
		if (!updateCookies) {
			return;
		}
		HttpHeader cookie = response.getHeader (HttpHeaders.SET_COOKIE);
		if (cookie == null) {
			return;
		}
		cookies = cookie.getValues ();
	}
	
	protected void addResponseHeaders (HttpResponseImpl response, HttpURLConnection hc) {
		Map<String, List<String>> httpHeaders = hc.getHeaderFields ();
		if (httpHeaders == null || httpHeaders.isEmpty ()) {
			return;
		}
		List<HttpHeader> headers = new ArrayList<HttpHeader> ();
		for (Entry<String, List<String>> header : httpHeaders.entrySet ()) {
			headers.add (new HttpHeaderImpl (header.getKey (), header.getValue ()));
		}
		response.setHeaders (headers);
	} 

}
