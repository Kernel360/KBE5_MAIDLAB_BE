package kernel.maidlab.api.point.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import kernel.maidlab.common.entity.point.Point;

public interface PointRepository extends JpaRepository<Point, Long>, PointRepositoryCustom {

}
