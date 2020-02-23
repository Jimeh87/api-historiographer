package com.jimrennie.apihistoriographer.service.core.applicationproxy;

import com.jimrennie.apihistoriographer.service.core.applicationproxy.observer.ApplicationObserver;
import com.jimrennie.apihistoriographer.service.util.JsonToMapConverter;
import lombok.*;
import lombok.experimental.Accessors;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.*;

@Getter
@ToString
@EqualsAndHashCode(of = "id")
@Accessors(chain = true)
@Entity
@Table(name = "application_proxy")
public class ApplicationProxy {

	@Id
	@Type(type = "uuid-char")
	@Column(name = "id")
	private UUID id;

	@Setter(AccessLevel.NONE)
	@Column(name = "application", nullable = false)
	private String application;

	@Setter
	@Column(name = "scheme", nullable = false)
	private String scheme;

	@Setter
	@Column(name = "host", nullable = false)
	private String host;

	@Setter
	@Column(name = "port", nullable = false)
	private Integer port;

	@Setter
	@Column(name = "header_blacklist")
	@Convert(converter = JsonToMapConverter.class)
	private Map<String, List<String>> headerBlacklist = new HashMap<>();

	@Setter(AccessLevel.NONE)
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
	@JoinColumn(name = "application_proxy_id")
	private Set<ApplicationObserver> applicationObservers = new HashSet<>();

	ApplicationProxy() {
	}

	public static ApplicationProxy newInstance(String application) {
		ApplicationProxy applicationProxy = new ApplicationProxy();
		applicationProxy.application = application;
		return applicationProxy;
	}

	@PrePersist
	public void prePersist() {
		id = UUID.randomUUID();
	}

}
