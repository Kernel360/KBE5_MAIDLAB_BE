package kernel.maidlab.api.point.repository;

import kernel.maidlab.common.entity.point.Point;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PointRepository extends JpaRepository<Point, Long>,PointRepositoryCustom {

}
