package kernel.maidlab.api.consumer.repository;

import kernel.maidlab.common.entity.consumer.Consumer;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ConsumerRepository extends JpaRepository<Consumer, Long> {
	Optional<Consumer> findByUuid(String uuid);

	Optional<Consumer> findByPhoneNumber(String phoneNumber);
}
