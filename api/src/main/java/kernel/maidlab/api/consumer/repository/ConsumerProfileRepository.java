package kernel.maidlab.api.consumer.repository;

import kernel.maidlab.api.consumer.entity.ConsumerProfile;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConsumerProfileRepository extends JpaRepository<ConsumerProfile, Long> {
}
