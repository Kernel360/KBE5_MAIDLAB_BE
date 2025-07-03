package kernel.maidlab.api.point.service;

import jakarta.servlet.http.HttpServletRequest;
import kernel.maidlab.api.auth.jwt.JwtFilter;
import kernel.maidlab.api.point.repository.PointRepository;
import kernel.maidlab.common.dto.point.request.PointRecordRequestDto;
import kernel.maidlab.common.dto.point.response.PageResponseDto;
import kernel.maidlab.common.dto.point.response.PointRecordResponseDto;
import kernel.maidlab.common.dto.point.response.PointResponseDto;
import kernel.maidlab.common.entity.consumer.Consumer;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class PointServiceImpl implements PointService{

    private final PointRepository pointRepository;

    @Override
    public PointResponseDto getPoint(HttpServletRequest request) {

        Consumer consumer = (Consumer) request.getAttribute(JwtFilter.CURRENT_USER_KEY);
        Long totalPointsByConsumerId = pointRepository.getTotalPointsByConsumerId(consumer.getId());
        return PointResponseDto.from(totalPointsByConsumerId);
    }

    @Override
    public PageResponseDto<PointRecordResponseDto> getPointRecordList(HttpServletRequest request, PointRecordRequestDto requestDto) {

        Pageable pageable = PageRequest.of(
                requestDto.getPageable().getPage(),
                requestDto.getPageable().getSize(),
                Sort.by(
                        requestDto.getPageable().getSort().stream()
                                .map(sortRequest ->
                                        new Sort.Order(
                                                sortRequest.getDirection(),
                                                sortRequest.getProperty()
                                        )
                                )
                                .toList()
                )
        );

        Consumer consumer = (Consumer) request.getAttribute(JwtFilter.CURRENT_USER_KEY);

        LocalDate startOfMonth = LocalDate.now().plusMonths(requestDto.getMonthOffset()).withDayOfMonth(1);
        LocalDate endOfMonth = startOfMonth.withDayOfMonth(startOfMonth.lengthOfMonth());

        Page<PointRecordResponseDto> page = pointRepository.findPointRecords(
                consumer.getId(),
                requestDto.getMonthOffset(),
                requestDto.getPointType(),
                pageable
        );
        return PageResponseDto.<PointRecordResponseDto>builder()
                .content(page.getContent())
                .hasNext(page.hasNext())
                .build();
    }


}
