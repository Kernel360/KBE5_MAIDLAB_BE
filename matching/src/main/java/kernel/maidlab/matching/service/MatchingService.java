package kernel.maidlab.matching.service;

public interface MatchingService {
	void StartMatching(Long reservationId);

	void AcceptMatching(Long reservationId, Long managerId);

	void RejectMatching(Long reservationId, Long managerId);

	void MatchingCanceled(Long reservationId);
}
