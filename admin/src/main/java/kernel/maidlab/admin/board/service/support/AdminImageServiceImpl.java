package kernel.maidlab.admin.board.service.support;

import kernel.maidlab.domain.board.entity.BoardImage;
import kernel.maidlab.domain.board.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminImageServiceImpl implements AdminImageService {

	private final ImageRepository imageRepository;

	public List<BoardImage> findAllByBoardId(Long boardId) {
		return imageRepository.findAllByBoardId(boardId);
	}
}
