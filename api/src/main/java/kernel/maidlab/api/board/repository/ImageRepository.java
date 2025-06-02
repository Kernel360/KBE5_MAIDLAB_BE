package kernel.maidlab.api.board.repository;

import kernel.maidlab.api.board.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {}
