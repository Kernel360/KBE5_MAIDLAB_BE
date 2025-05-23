package kernel.maidlab.reservation.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import kernel.maidlab.reservation.dto.response.AvailableManagerDto;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService {
	@Override
	public List<AvailableManagerDto> getAvailableManagersList(
		LocalDateTime startTime
	){
		AvailableManagerDto mockManager = new AvailableManagerDto( 1L,"홍길동", "강남구",4,"야야야");
		System.out.println(startTime);
		List<AvailableManagerDto> availableMangerList = new ArrayList<>();
		availableMangerList.add(mockManager);
		return availableMangerList;
	}
}
