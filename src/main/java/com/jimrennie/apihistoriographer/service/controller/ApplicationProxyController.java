package com.jimrennie.apihistoriographer.service.controller;

import com.jimrennie.apihistoriographer.service.core.ProxyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RequestMapping("/api/v1/applications/{application}/proxy")
@RestController
public class ApplicationProxyController {

	// TODO: Add lookahead and require slash after proxy if additional data exists
	public static final Pattern URL_PROXY_PATTERN = Pattern.compile("^\\/api\\/v1\\/applications\\/.+\\/proxy\\/?(.*)$");

	@Autowired
	private ProxyService proxyService;

	@RequestMapping("/**")
	public ResponseEntity<String> proxy(@PathVariable("application") String application, @RequestBody(required = false) String body, HttpMethod method, HttpServletRequest request) {
		return proxyService.proxyBuilder()
			.application(application)
			.method(method)
			.headers(extractRequestHeaders(request))
			.body(body)
			.path(extractProxyRequestPath(request))
			.query(request.getQueryString())
			.request();
	}

	private HttpHeaders extractRequestHeaders(HttpServletRequest request) {
		HttpHeaders headers = new HttpHeaders();
		Enumeration<String> headerNames = request.getHeaderNames();
		while (headerNames.hasMoreElements()) {
			String headerName = headerNames.nextElement();
			headers.set(headerName, request.getHeader(headerName));
		}
		return headers;
	}

	private String extractProxyRequestPath(HttpServletRequest request) {
		Matcher matcher = URL_PROXY_PATTERN.matcher(request.getRequestURI());
		if (matcher.find()) {
			return matcher.group(1);
		}
		throw new IllegalStateException("Request uri not found in " + request.getRequestURI());
	}
}
