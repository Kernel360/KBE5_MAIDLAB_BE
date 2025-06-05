package kernel.maidlab.admin.event.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import kernel.maidlab.admin.event.entity.Event;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

	@Query("SELECT e FROM Event e ORDER BY e.createAt DESC")
	List<Event> findAllEventsOrderByCreatedAtDesc();

}