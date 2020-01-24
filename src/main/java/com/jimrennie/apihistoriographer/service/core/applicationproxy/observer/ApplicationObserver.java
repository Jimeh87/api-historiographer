package com.jimrennie.apihistoriographer.service.core.applicationproxy.observer;

import com.jimrennie.apihistoriographer.service.core.applicationproxy.RestParameter;
import lombok.*;
import lombok.experimental.Accessors;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
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

	@Column(name = "path", nullable = false)
	private String path;

	@Column(name = "method", nullable = false)
	private String method;

	@Setter
	@Column(name = "body")
	@Lob
	@Basic(fetch = FetchType.EAGER)
	private String body;

	@Column(name = "pollingIntervalMinutes", nullable = false)
	private Integer pollingIntervalMinutes;

	@Setter(AccessLevel.NONE)
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
	@JoinColumn(name = "query_param_app_observer_id")
	private Set<RestParameter> queryParameters = new HashSet<>();

	@Setter(AccessLevel.NONE)
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
	@JoinColumn(name = "header_app_observer_id")
	private Set<RestParameter> headers = new HashSet<>();

	@PrePersist
	public void prePersist() {
		id = UUID.randomUUID();
	}

}
