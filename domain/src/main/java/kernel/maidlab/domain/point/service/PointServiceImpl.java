package kernel.maidlab.domain.point.service;

import jakarta.servlet.http.HttpServletRequest;
import kernel.maidlab.domain.consumer.entity.Consumer;
import kernel.maidlab.domain.point.dto.request.PointChargeRequestDto;
import kernel.maidlab.domain.point.dto.request.PointRecordRequestDto;
import kernel.maidlab.domain.point.dto.response.PageResponseDto;
import kernel.maidlab.domain.point.dto.response.PointRecordResponseDto;
import kernel.maidlab.domain.point.dto.response.PointResponseDto;
import kernel.maidlab.domain.point.entity.Point;
import kernel.maidlab.domain.point.repository.PointRepository;
import kernel.maidlab.domain.util.UserValidator;
import kernel.maidlab.common.enums.UserType;
import kernel.maidlab.core.security.AuthenticationHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PointServiceImpl implements PointService {

	private final PointRepository pointRepository;
	private final UserValidator userValidator;

	@Override
	public PointResponseDto getPoint(HttpServletRequest request) {

		String userId = AuthenticationHelper.getCurrentUserKey();
		Consumer consumer = (Consumer)userValidator.findByUuid(userId, UserType.CONSUMER);
		Long totalPointsByConsumerId = pointRepository.getTotalPointsByConsumerId(consumer.getId());
		return PointResponseDto.from(totalPointsByConsumerId);
	}

	@Override
	public PageResponseDto<PointRecordResponseDto> getPointRecordList(
		HttpServletRequest request,
		PointRecordRequestDto requestDto) {
		Integer page = requestDto.getPageable().getPage();
		Integer size = requestDto.getPageable().getSize();
		List<PointRecordRequestDto.PageableRequest.SortRequest> sort = requestDto.getPageable().getSort();

		Pageable pageable = PageRequest.of(
			page,
			size,
			Sort.by(
				sort.stream()
					.map(sortRequest ->
						new Sort.Order(
							sortRequest.getDirection(),
							sortRequest.getProperty()
						)
					)
					.toList()
			)
		);

		String userId = AuthenticationHelper.getCurrentUserKey();
		Consumer consumer = (Consumer)userValidator.findByUuid(userId, UserType.CONSUMER);

		LocalDate startOfMonth = LocalDate.now().plusMonths(requestDto.getMonthOffset()).withDayOfMonth(1);
		LocalDate endOfMonth = startOfMonth.withDayOfMonth(startOfMonth.lengthOfMonth());

		Page<PointRecordResponseDto> pointRecordsPage = pointRepository.findPointRecords(
			consumer.getId(),
			requestDto.getMonthOffset(),
			requestDto.getPointType(),
			pageable
		);

		return PageResponseDto.<PointRecordResponseDto>builder()
			.content(pointRecordsPage.getContent())
			.hasNext(pointRecordsPage.hasNext())
			.build();
	}

	public void chargePoint(
		HttpServletRequest request,
		PointChargeRequestDto pointChargeRequestDto
	) {
		String userId = AuthenticationHelper.getCurrentUserKey();
		Consumer consumer = userValidator.findByUuid(userId, UserType.CONSUMER);

		if (pointChargeRequestDto.getChargeAmount() > 0) {
			Point chargedPoint = Point.createChargePoint(
				consumer,
				pointChargeRequestDto.getChargeAmount());

			pointRepository.save(chargedPoint);
		}

	}

}
