package kernel.maidlab.admin.board.service.support;

import java.util.List;

import org.springframework.stereotype.Service;

import kernel.maidlab.api.board.repository.ImageRepository;
import kernel.maidlab.common.entity.board.Image;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminImageServiceImpl implements AdminImageService {

	private final ImageRepository imageRepository;

	public List<Image> findAllByBoardId(Long boardId) {
		return imageRepository.findAllByBoardId(boardId);
	}
}
