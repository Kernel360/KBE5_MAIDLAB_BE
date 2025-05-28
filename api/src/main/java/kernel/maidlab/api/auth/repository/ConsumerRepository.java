package kernel.maidlab.api.auth.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import kernel.maidlab.api.auth.entity.Consumer;

public interface ConsumerRepository extends JpaRepository<Consumer, Long> {
	Optional<Consumer> findByUuid(String uuid);
}
