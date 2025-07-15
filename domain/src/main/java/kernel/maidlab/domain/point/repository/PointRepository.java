package kernel.maidlab.domain.point.repository;

import kernel.maidlab.domain.point.entity.Point;
import org.springframework.data.jpa.repository.JpaRepository;



public interface PointRepository extends JpaRepository<Point, Long>, PointRepositoryCustom {

}
