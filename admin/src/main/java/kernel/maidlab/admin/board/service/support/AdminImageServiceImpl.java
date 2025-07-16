package kernel.maidlab.admin.board.service.support;

import java.util.List;

import org.springframework.stereotype.Service;

import kernel.maidlab.core.aop.annotation.exception.ExceptionHandler;
import kernel.maidlab.core.aop.enums.LogLevel;
import kernel.maidlab.common.enums.ResponseType;
import kernel.maidlab.domain.board.entity.BoardImage;
import kernel.maidlab.domain.board.repository.ImageRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminImageServiceImpl implements AdminImageService {

	private final ImageRepository imageRepository;

	@ExceptionHandler(
		value = {RuntimeException.class},
		responseType = ResponseType.DATABASE_ERROR,
		message = "이미지 조회 중 오류가 발생했습니다",
		logLevel = LogLevel.WARN
	)
	public List<BoardImage> findAllByBoardId(Long boardId) {
		return imageRepository.findAllByBoardId(boardId);
	}
}
