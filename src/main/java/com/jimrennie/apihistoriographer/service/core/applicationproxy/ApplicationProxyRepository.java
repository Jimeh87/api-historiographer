package com.jimrennie.apihistoriographer.service.core.applicationproxy;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ApplicationProxyRepository extends JpaRepository<ApplicationProxy, UUID> {

	Optional<ApplicationProxy> findByApplication(String application);

}
