package com.jimrennie.apihistoriographer.service.core.applicationproxy.observer;

import com.jimrennie.apihistoriographer.service.util.JsonToMapConverter;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Getter
@ToString
@EqualsAndHashCode(of = "id")
@Accessors(chain = true)
@Entity
@Table(name = "application_observer")
public class ApplicationObserver {

	@Id
	@Type(type = "uuid-char")
	@Column(name = "id")
	private UUID id;

	@Column(name = "key", nullable = false)
	private String key;

	@Setter
	@Column(name = "headers")
	@Convert(converter = JsonToMapConverter.class)
	private Map<String, List<String>> headers = new HashMap<>();

	@Setter
	@Column(name = "path", nullable = false)
	private String path;

	@Setter
	@Column(name = "query_parameters")
	@Convert(converter = JsonToMapConverter.class)
	private Map<String, List<String>> queryParameters = new HashMap<>();

	@Setter
	@Column(name = "method", nullable = false)
	private String method;

	@Setter
	@Column(name = "body")
	@Lob
	@Basic(fetch = FetchType.EAGER)
	private String body;

	ApplicationObserver() {
	}

	@PrePersist
	public void prePersist() {
		id = UUID.randomUUID();
	}

	public static ApplicationObserver newInstance(String key) {
		ApplicationObserver applicationObserver = new ApplicationObserver();
		applicationObserver.key = key;
		return applicationObserver;
	}

}
