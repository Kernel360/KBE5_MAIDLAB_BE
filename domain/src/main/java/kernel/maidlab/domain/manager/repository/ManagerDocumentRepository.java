package kernel.maidlab.domain.manager.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import kernel.maidlab.domain.manager.entity.ManagerDocument;

@Repository
public interface ManagerDocumentRepository extends JpaRepository<ManagerDocument, Long> {
	List<ManagerDocument> findByManagerId(Long managerId);

}
