package kernel.maidlab.api.consumer.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import kernel.maidlab.common.entity.consumer.Consumer;

public interface ConsumerRepository extends JpaRepository<Consumer, Long> {
	Optional<Consumer> findByUuid(String uuid);

	Optional<Consumer> findByPhoneNumber(String phoneNumber);
}
