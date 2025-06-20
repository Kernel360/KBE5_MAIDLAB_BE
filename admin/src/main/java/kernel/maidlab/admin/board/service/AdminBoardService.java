package kernel.maidlab.admin.board.service;

import java.nio.file.AccessDeniedException;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

import jakarta.servlet.http.HttpServletRequest;
import kernel.maidlab.common.dto.ResponseDto;
import kernel.maidlab.common.dto.board.request.AnswerRequestDto;
import kernel.maidlab.common.dto.board.response.AdminBoardDetailResponseDto;
import kernel.maidlab.common.dto.board.response.AdminBoardResponseDto;
import kernel.maidlab.common.dto.board.response.BoardDetailResponseDto;
import kernel.maidlab.common.dto.board.response.BoardResponseDto;

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
}
