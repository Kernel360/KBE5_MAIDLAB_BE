package kernel.maidlab.api.point.repository;

import kernel.maidlab.api.point.entity.Point;
import org.springframework.data.jpa.repository.JpaRepository;



public interface PointRepository extends JpaRepository<Point, Long>, PointRepositoryCustom {

}
