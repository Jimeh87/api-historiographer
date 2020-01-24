package com.jimrennie.apihistoriographer.service.core.proxy;

import com.jimrennie.apihistoriographer.service.core.applicationproxy.ApplicationHeaderService;
import com.jimrennie.apihistoriographer.service.core.applicationproxy.ApplicationProxyService;
import com.jimrennie.apihistoriographer.service.core.applicationproxy.ApplicationUriService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class ProxyInterceptor implements ClientHttpRequestInterceptor {

	@Autowired
	private ApplicationProxyService applicationProxyService;
	@Autowired
	private ApplicationUriService applicationUriService;
	@Autowired
	private ApplicationHeaderService applicationHeaderService;

	@Override
	public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
//		String application = request.getHeaders().get("p-application").stream().findAny().orElseThrow(() -> new IllegalArgumentException("Missing p-application header"));
//		request.getHeaders().set("host", applicationProxyConfigService.getConfig(application).getHost());

		ClientHttpResponse response = execution.execute(request, body);

		return response;
	}
}
