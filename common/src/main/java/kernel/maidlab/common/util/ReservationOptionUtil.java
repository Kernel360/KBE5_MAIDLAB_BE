package kernel.maidlab.common.util;

import java.util.List;
import java.util.stream.Collectors;

import kernel.maidlab.common.dto.reservation.request.ReservationRequestDto.ServiceOptionRequest;

public class ReservationOptionUtil {

	public static String serializeOptions(List<ServiceOptionRequest> options) {
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
