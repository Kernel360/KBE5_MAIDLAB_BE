package kernel.maidlab.admin.auth.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import kernel.maidlab.admin.auth.entity.Admin;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {
	Optional<Admin> findByAdminKey(String adminKey);

	Optional<Admin> findByAdminKeyAndIsDeletedFalse(String adminKey);
}
