package com.jimrennie.apihistoriographer.service.core.applicationproxy.assembler;

import com.jimrennie.apihistoriographer.service.controller.api.ApplicationProxyDto;
import com.jimrennie.apihistoriographer.service.core.applicationproxy.ApplicationProxy;
import com.jimrennie.apihistoriographer.service.core.applicationproxy.ApplicationProxyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ApplicationProxyAssembler {

	@Autowired
	private ApplicationProxyRepository applicationProxyRepository;
	@Autowired
	private ApplicationObserverAssembler applicationObserverAssembler;

	public ApplicationProxy assemble(ApplicationProxyDto applicationProxyDto) {
		return assembleOnto(
				applicationProxyRepository.findByApplication(applicationProxyDto.getApplication())
						.orElseGet(() -> ApplicationProxy.newInstance(applicationProxyDto.getApplication())),
				applicationProxyDto);
	}

	private ApplicationProxy assembleOnto(ApplicationProxy applicationProxy, ApplicationProxyDto applicationProxyDto) {
		applicationObserverAssembler.assembleOnto(applicationProxy.getApplicationObservers(), applicationProxyDto.getObservers());

		return applicationProxy
				.setScheme(applicationProxyDto.getScheme())
				.setHost(applicationProxyDto.getHost())
				.setPort(applicationProxyDto.getPort())
				.setHeaderBlacklist(applicationProxyDto.getHeaderBlacklist());
	}

	public ApplicationProxyDto disassemble(ApplicationProxy applicationProxy) {
		return new ApplicationProxyDto()
				.setApplication(applicationProxy.getApplication())
				.setScheme(applicationProxy.getScheme())
				.setHost(applicationProxy.getHost())
				.setPort(applicationProxy.getPort())
				.setHeaderBlacklist(applicationProxy.getHeaderBlacklist());
	}

}
