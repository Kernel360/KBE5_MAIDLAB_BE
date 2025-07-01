package kernel.maidlab.admin.reservation.repository;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface AdminReservationRepositoryCustom {

	Long countByReservationDate(LocalDate today);

	BigDecimal sumTotalPrice(Long consumerId);

}
