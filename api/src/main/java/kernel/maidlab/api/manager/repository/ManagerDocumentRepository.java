package kernel.maidlab.api.manager.repository;

import kernel.maidlab.api.manager.entity.ManagerDocument;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ManagerDocumentRepository extends JpaRepository<ManagerDocument, Long> {
	List<ManagerDocument> findByManagerId(Long managerId);

}