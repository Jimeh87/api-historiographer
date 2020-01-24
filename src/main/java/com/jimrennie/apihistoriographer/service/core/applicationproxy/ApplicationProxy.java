package com.jimrennie.apihistoriographer.service.core.applicationproxy;

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
@Table(name = "application_proxy")
public class ApplicationProxy {

	@Id
	@Type(type = "uuid-char")
	@Column(name = "id")
	private UUID id;

	@Column(name = "application", nullable = false)
	private String application;

	@Column(name = "scheme", nullable = false)
	private String scheme;

	@Column(name = "host", nullable = false)
	private String host;

	@Column(name = "port", nullable = false)
	private Integer port;

	@Setter(AccessLevel.NONE)
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
	@JoinColumn(name = "header_blacklist_app_proxy_id")
	private Set<RestParameter> headerBlacklist = new HashSet<>();

	@PrePersist
	public void prePersist() {
		id = UUID.randomUUID();
	}

}
