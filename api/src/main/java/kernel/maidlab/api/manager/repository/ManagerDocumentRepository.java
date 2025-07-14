package kernel.maidlab.api.manager.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import kernel.maidlab.common.entity.manager.ManagerDocument;

@Repository
public interface ManagerDocumentRepository extends JpaRepository<ManagerDocument, Long> {
	List<ManagerDocument> findByManagerId(Long managerId);

}
