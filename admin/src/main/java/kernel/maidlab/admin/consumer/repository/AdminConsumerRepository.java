package kernel.maidlab.admin.consumer.repository;

import kernel.maidlab.domain.consumer.entity.Consumer;
import org.springframework.data.jpa.repository.JpaRepository;


public interface AdminConsumerRepository extends JpaRepository<Consumer, Long> {

	Long countByIsDeletedFalse();
}
