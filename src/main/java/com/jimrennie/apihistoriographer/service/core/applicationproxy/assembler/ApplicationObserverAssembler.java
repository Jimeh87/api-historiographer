package com.jimrennie.apihistoriographer.service.core.applicationproxy.assembler;

import com.jimrennie.apihistoriographer.service.controller.api.ApplicationObserverDto;
import com.jimrennie.apihistoriographer.service.core.applicationproxy.observer.ApplicationObserver;
import com.jimrennie.apihistoriographer.service.util.DtoToEntityCollectionMerger;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Component
public class ApplicationObserverAssembler {

	private final DtoToEntityCollectionMerger<ApplicationObserverDto, ApplicationObserver> observerMerger = DtoToEntityCollectionMerger.of(
			(dto, entity) -> dto.getKey().equals(entity.getKey()),
			dto -> ApplicationObserver.newInstance(dto.getKey()),
			(dto, entity) -> entity
					.setHeaders(dto.getHeaders())
					.setPath(dto.getPath())
					.setQueryParameters(dto.getQueryParameters())
					.setMethod(dto.getMethod())
					.setBody(dto.getBody())
					.setPollingIntervalMinutes(dto.getPollingIntervalMinutes())
	);

	public void assembleOnto(Set<ApplicationObserver> applicationObservers, List<ApplicationObserverDto> applicationObserverDtos) {
		observerMerger.accept(applicationObserverDtos, applicationObservers);
	}

}
