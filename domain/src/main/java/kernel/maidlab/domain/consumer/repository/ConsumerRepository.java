package kernel.maidlab.domain.consumer.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import kernel.maidlab.domain.consumer.entity.Consumer;

public interface ConsumerRepository extends JpaRepository<Consumer, Long> {
	Optional<Consumer> findByUuid(String uuid);

	Optional<Consumer> findByPhoneNumber(String phoneNumber);
}
