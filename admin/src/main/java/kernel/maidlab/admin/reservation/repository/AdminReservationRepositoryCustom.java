package kernel.maidlab.admin.reservation.repository;

import java.time.LocalDate;

public interface AdminReservationRepositoryCustom {

	Long countByReservationDate(LocalDate today);

}
