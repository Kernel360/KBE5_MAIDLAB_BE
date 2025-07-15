package kernel.maidlab.domain.point.repository;

import kernel.maidlab.domain.point.dto.response.PointRecordResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PointRepositoryCustom {

	Long getTotalPointsByConsumerId(Long consumerId);

	Page<PointRecordResponseDto> findPointRecords(
		Long consumerId,
		Integer monthOffset,
		String pointType,
		Pageable pageable
	);
}
