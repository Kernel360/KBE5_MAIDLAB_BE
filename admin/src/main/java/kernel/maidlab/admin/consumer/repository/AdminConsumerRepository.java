package kernel.maidlab.admin.consumer.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import kernel.maidlab.common.entity.consumer.Consumer;

public interface AdminConsumerRepository extends JpaRepository<Consumer, Long> {

}
