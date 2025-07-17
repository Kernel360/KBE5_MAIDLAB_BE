package kernel.maidlab.domain.consumer.repository;

import kernel.maidlab.domain.consumer.entity.Consumer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ConsumerRepository extends JpaRepository<Consumer, Long> {
    Optional<Consumer> findByUuid(String uuid);

    Optional<Consumer> findByPhoneNumber(String phoneNumber);
}
