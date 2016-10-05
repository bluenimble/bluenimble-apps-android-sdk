package com.bluenimble.apps.sdk.http.request;

import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Set;

import com.bluenimble.apps.sdk.http.HttpHeader;
import com.bluenimble.apps.sdk.http.HttpMessage;
import com.bluenimble.apps.sdk.http.HttpMessageBody;
import com.bluenimble.apps.sdk.http.HttpParameter;

public interface HttpRequest extends HttpMessage {
	
	String 				getId();
	String 				getName();
	URI 				getURI() throws UnsupportedEncodingException, URISyntaxException;

	void 				setCharset(String charset);
	
	void 				setContentType(String contentType);
	@Override
	String 				getContentType();

	boolean 			isCachingEnabled();
	void 				setCachingEnabled(boolean cachingEnabled);
	
	int					getConnectTimeout();
	void				setConnectTimeout(int timeout);
	
	int					getReadTimeout();
	void				setReadTimeout(int timeout);

	void 				setHeaders(List<HttpHeader> headers);

	List<HttpParameter>	getParameters();
	void 				setParameters(List<HttpParameter> parameters);
	
	void				setBody(HttpMessageBody body);

	void				write(HttpURLConnection hc) throws HttpRequestWriteException;
	
	void				setSuccessCodes(Set<String> codes);
	Set<String>			getSuccessCodes();

	boolean 			isDebugMode();
	void 				setDebugMode(boolean debugMode);

	Proxy 				getProxy();
	
	void 				setVisitor(HttpRequestVisitor visitor);
	HttpRequestVisitor 	getVisitor();
	
	String []			getSniHosts();
	void				setSniHosts(String[] hosts);

}
