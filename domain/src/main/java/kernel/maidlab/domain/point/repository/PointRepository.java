package kernel.maidlab.domain.point.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import kernel.maidlab.domain.point.entity.Point;

public interface PointRepository extends JpaRepository<Point, Long>, PointRepositoryCustom {

}
