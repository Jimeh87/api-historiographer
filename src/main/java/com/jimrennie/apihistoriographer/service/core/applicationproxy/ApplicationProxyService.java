package com.jimrennie.apihistoriographer.service.core.applicationproxy;

import com.jimrennie.apihistoriographer.service.controller.api.ApplicationProxyDto;
import com.jimrennie.apihistoriographer.service.core.applicationproxy.assembler.ApplicationProxyAssembler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.UUID;

@Service
public class ApplicationProxyService {

	@Autowired
	private ApplicationProxyAssembler applicationProxyAssembler;
	@Autowired
	private ApplicationProxyRepository applicationProxyRepository;

	public ApplicationProxyDto get(String application) {
		return applicationProxyRepository.findByApplication(application)
				.map(applicationProxyAssembler::disassemble)
				.orElseThrow(() -> new IllegalArgumentException(String.format("Application [%s] not found", application)));
	}

	@Transactional
	public UUID save(ApplicationProxyDto applicationProxyDto) {
		return applicationProxyRepository.save(applicationProxyAssembler.assemble(applicationProxyDto))
				.getId();
	}

}
