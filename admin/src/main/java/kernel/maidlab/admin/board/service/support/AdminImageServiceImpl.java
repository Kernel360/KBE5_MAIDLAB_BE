package kernel.maidlab.admin.board.service.support;

import java.util.List;

import org.springframework.stereotype.Service;

import kernel.maidlab.domain.board.entity.BoardImage;
import kernel.maidlab.domain.board.repository.ImageRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminImageServiceImpl implements AdminImageService {

	private final ImageRepository imageRepository;

	public List<BoardImage> findAllByBoardId(Long boardId) {
		return imageRepository.findAllByBoardId(boardId);
	}
}
