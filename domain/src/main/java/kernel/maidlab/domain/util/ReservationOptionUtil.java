package kernel.maidlab.domain.util;

import java.util.List;
import java.util.stream.Collectors;

import kernel.maidlab.domain.reservation.dto.request.ReservationRequestDto;

public class ReservationOptionUtil {

	public static String serializeOptions(List<ReservationRequestDto.ServiceOptionRequest> options) {
		if (options == null || options.isEmpty())
			return "";

		return options.stream().map(opt -> {
			if (opt.getCount() != null) {
				return opt.getId() + ":" + opt.getCount();
			}
			return opt.getId();
		}).collect(Collectors.joining(","));
	}
}
