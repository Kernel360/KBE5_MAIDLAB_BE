package kernel.maidlab.api.consumer.repository;

import kernel.maidlab.api.auth.entity.Consumer;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ConsumerRepository extends JpaRepository<Consumer, Long> {
	Optional<Consumer> findByUuid(String uuid);

	Optional<Consumer> findByPhoneNumber(String phoneNumber);
}
