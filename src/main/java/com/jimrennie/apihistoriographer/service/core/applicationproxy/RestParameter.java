package com.jimrennie.apihistoriographer.service.core.applicationproxy;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.UUID;

@Getter
@ToString
@EqualsAndHashCode(of = "id")
@Accessors(chain = true)
@Entity
@Table(name = "rest_parameter")
public class RestParameter {

	@Id
	@Type(type = "uuid-char")
	@Column(name = "id")
	private UUID id;

	@Column(name = "parameter_key", nullable = false)
	private String key;

	@Column(name = "parameter_value", nullable = false)
	private String value;

	@PrePersist
	public void prePersist() {
		id = UUID.randomUUID();
	}

}
