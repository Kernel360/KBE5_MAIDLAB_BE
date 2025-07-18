package kernel.maidlab.admin.consumer.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import kernel.maidlab.domain.consumer.entity.Consumer;

public interface AdminConsumerRepository extends JpaRepository<Consumer, Long> {

	Long countByIsDeletedFalse();

	Page<Consumer> findByIsDeleted(Boolean isDeleted, Pageable pageable);
}
