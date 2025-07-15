package kernel.maidlab.admin.board.service;

import jakarta.servlet.http.HttpServletRequest;
import kernel.maidlab.api.board.dto.request.AnswerRequestDto;
import kernel.maidlab.api.board.dto.response.AdminBoardDetailResponseDto;
import kernel.maidlab.api.board.dto.response.AdminBoardResponseDto;
import kernel.maidlab.common.dto.ResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.AccessDeniedException;
import java.util.List;

public interface AdminBoardService {

	ResponseEntity<ResponseDto<List<AdminBoardResponseDto>>> getAllRefundBoardList(HttpServletRequest request, int page,
																				   int size);

	ResponseEntity<ResponseDto<AdminBoardDetailResponseDto>> adminGetConsumerBoard(
		HttpServletRequest request,
		Long boardId
	) throws AccessDeniedException;

	ResponseEntity<ResponseDto<List<AdminBoardResponseDto>>> getAllConsultationBoardList(HttpServletRequest request,
		int page, int size);

	ResponseEntity<ResponseDto<Void>> createAnswer(AnswerRequestDto requestDto, HttpServletRequest request,
												   Long boardId);

	@Transactional
	ResponseEntity<ResponseDto<Void>> modifyAnswer(AnswerRequestDto requestDto, Long answerId);

	Long getRefundBoardWithoutAnswerCount(HttpServletRequest request);

	Long getCounselBoardWithoutAnswerCount(HttpServletRequest request);
}
