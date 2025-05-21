package kernel.maidlab.consumer.repository;

import kernel.maidlab.consumer.entity.ConsumerProfile;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConsumerProfileRepository extends JpaRepository<ConsumerProfile, Long> {
}
