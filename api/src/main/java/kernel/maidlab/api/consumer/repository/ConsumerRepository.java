package kernel.maidlab.api.consumer.repository;

import kernel.maidlab.api.auth.entity.Consumer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository("authConsumerRepository")
public interface ConsumerRepository extends JpaRepository<Consumer, Long> {

    Optional<Consumer> findByUuid(String uuid);
}
