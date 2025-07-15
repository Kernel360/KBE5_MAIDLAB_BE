package kernel.maidlab.domain.point.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import kernel.maidlab.domain.point.dto.response.PointRecordResponseDto;

public interface PointRepositoryCustom {

	Long getTotalPointsByConsumerId(Long consumerId);

	Page<PointRecordResponseDto> findPointRecords(
		Long consumerId,
		Integer monthOffset,
		String pointType,
		Pageable pageable
	);
}
