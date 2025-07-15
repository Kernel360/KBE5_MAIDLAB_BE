package kernel.maidlab.api.board.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import kernel.maidlab.api.board.dto.BoardQueryDto;
import kernel.maidlab.api.board.dto.ImageDto;
import kernel.maidlab.api.board.dto.request.BoardRequestDto;
import kernel.maidlab.api.board.dto.request.BoardUpdateRequestDto;
import kernel.maidlab.api.board.dto.response.BoardDetailResponseDto;
import kernel.maidlab.api.board.dto.response.BoardResponseDto;
import kernel.maidlab.api.board.entity.Board;
import kernel.maidlab.api.board.entity.BoardImage;
import kernel.maidlab.api.board.repository.BoardRepository;
import kernel.maidlab.api.board.repository.ImageRepository;
import kernel.maidlab.api.consumer.entity.Consumer;
import kernel.maidlab.api.consumer.repository.ConsumerRepository;
import kernel.maidlab.api.manager.entity.Manager;
import kernel.maidlab.api.manager.repository.ManagerRepository;
import kernel.maidlab.api.util.UserValidator;
import kernel.maidlab.common.enums.UserType;
import kernel.maidlab.core.aop.aspect.auth.AuthenticationAspect;
import kernel.maidlab.core.security.AuthenticationHelper;
import kernel.maidlab.core.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService {

	private final BoardRepository boardRepository;
	private final ImageRepository imageRepository;
	private final ConsumerRepository consumerRepository;
	private final ManagerRepository managerRepository;
	private final UserValidator userValidator;

	// 게시판 글 생성
	public void createBoard(
		HttpServletRequest request,
		BoardRequestDto boardRequestDto) {

		String userId = AuthenticationHelper.getCurrentUserKey();
		UserType userType = AuthenticationHelper.getCurrentUserType();
		Object user = userValidator.findByUuid(userId, userType);

		Board board = Board.createBoard(user, boardRequestDto);
		Board savedBoard = boardRepository.save(board);

		boardRequestDto.getImages()
			.forEach((imageDto) -> imageRepository.save(
				new BoardImage(
					board,
					imageDto.getImagePath(),
					imageDto.getName())));
		log.info("게시글 생성 완료 - 게시글 ID: {}, 제목: {}", savedBoard.getId(), boardRequestDto.getTitle());
	}

	// 게시글 전체 조회
	@Transactional(readOnly = true)
	public List<BoardResponseDto> getConsumerBoardList(HttpServletRequest request) {

		String userId = AuthenticationHelper.getCurrentUserKey();
		UserType userType = AuthenticationHelper.getCurrentUserType();
		Object user = userValidator.findByUuid(userId, userType);

		CustomUserDetails currentUser = AuthenticationAspect.getCurrentUser();
		List<BoardQueryDto> boardQueryDtoList = getBoardQueryDtoList(currentUser, userType);

		return boardQueryDtoList.stream()
			.map(BoardResponseDto::from)
			.toList();
	}

	// 수요자 글 상세 조회
	@Transactional(readOnly = true)
	public BoardDetailResponseDto getConsumerBoard(
		HttpServletRequest request,
		Long boardId
	) throws AccessDeniedException {

		String userId = AuthenticationHelper.getCurrentUserKey();
		UserType userType = AuthenticationHelper.getCurrentUserType();
		Object user = userValidator.findByUuid(userId, userType);

		Board board = boardRepository.findByIdAndIsDeletedFalse(boardId)
			.orElseThrow(() -> new EntityNotFoundException("존재하지 않는 게시물 입니다."));

		// 토큰으로 찾은 수요자id와 PathVariable로 넘어온 게시판id로 찾은 consumerId와 비교
		if (!board.isAccessibleBy(user)) {
			throw new AccessDeniedException("해당 게시글에 접근할 권한이 없습니다.");
		}

		// 답변여부가 true면 답변까지 조회
		if (board.getIsAnswered()) {
			board = boardRepository.findBoardWithAnswerIfAnswered(boardId)
				.orElseThrow(() -> new EntityNotFoundException("답변이 존재하지 않습니다."));
		}

		List<BoardImage> boardImages = imageRepository.findAllByBoardId(boardId);

		return BoardDetailResponseDto.from(board, boardImages);

	}

	// 수정
	public void modifyBoard(
		HttpServletRequest request,
		Long boardId,
		BoardUpdateRequestDto boardUpdateRequestDto) {

		Board board = boardRepository.findByIdAndIsDeletedFalse(boardId)
			.orElseThrow(() -> new RuntimeException("게시글이 존재하지 않습니다."));

		// 사용자 검증
		CustomUserDetails user = getUser(request);
		if (!isUserBoardWriter(board, user)) {
			throw new RuntimeException("수정 권한이 없습니다.");
		}

		board.boardUpdate(boardUpdateRequestDto);

		List<BoardImage> currentBoardImages = imageRepository.findAllByBoardId(boardId);
		List<ImageDto> newImageDataList = boardUpdateRequestDto.getImages();

		updateImages(currentBoardImages, newImageDataList, board);
		log.info("게시글 수정 완료 - 게시글 ID: {}", boardId);

	}

	// 게시글 삭제
	public void deleteBoard(
		HttpServletRequest request,
		Long boardId
	) {
		Board board = boardRepository
			.findByIdAndIsDeletedFalse(boardId)
			.orElseThrow(() -> new EntityNotFoundException("존재하지 않는 게시판 입니다."));

		board.updateIsDelete(true);
		log.info("게시글 삭제 완료 - 게시글 ID: {}", boardId);
	}

	/**
	 *서비스 외의 로직
	 */
	// 사용자 접근 검증
	public boolean isUserBoardWriter(Board board, CustomUserDetails user) {
		if (user.getUserType() == UserType.CONSUMER) {
			Consumer consumer = userValidator.findByUuid(user.getUserKey(), UserType.CONSUMER);
			return board.getConsumer() != null && board.getConsumer().getId().equals(consumer.getId());
		} else if (user.getUserType() == UserType.MANAGER) {
			Manager manager = userValidator.findByUuid(user.getUserKey(), UserType.MANAGER);
			return board.getManager() != null && board.getManager().getId().equals(manager.getId());
		}
		return false;
	}

	// user타입에 따른 board 조회
	public List<BoardQueryDto> getBoardQueryDtoList(CustomUserDetails user, UserType userType) {

		if (userType == UserType.CONSUMER) {
			Consumer consumer = userValidator.findByUuid(user.getUserKey(), UserType.CONSUMER);
			return boardRepository.findAllByUserIdIsDeletedFalse(consumer.getId(), userType);

		} else if (userType == UserType.MANAGER) {
			Manager manager = userValidator.findByUuid(user.getUserKey(), UserType.MANAGER);
			return boardRepository.findAllByUserIdIsDeletedFalse(manager.getId(), userType);

		}
		throw new IllegalArgumentException("유효하지 않은 사용자 타입입니다: " + userType);
	}

	// 유저 찾기
	public CustomUserDetails getUser(HttpServletRequest request) {
		return AuthenticationAspect.getCurrentUser();
	}

	// 이미지 수정 로직
	// todo:너무 많은 역할을 담담하고 있음 - 추후 리펙토링 필요
	public void updateImages(List<BoardImage> currentBoardImages, List<ImageDto> newImageDataList, Board board) {

		// 사용자가 images null을 보낸 경우 기존 이미지만 삭제후 리턴
		if (newImageDataList == null) {
			imageRepository.deleteAllByBoard(board);
			board.getBoardImages().clear();
			return;
		}

		// 현재 이미지 ID 목록
		Set<Long> currentImageIds = currentBoardImages.stream()
			.map(BoardImage::getId)
			.collect(Collectors.toSet());

		// 사용자가 보낸 ID들 중 유효하지 않은 ID 체크
		List<Long> invalidIds = newImageDataList.stream()
			.map(ImageDto::getId)
			.filter(Objects::nonNull)
			.filter(id -> !currentImageIds.contains(id))
			.toList();

		if (!invalidIds.isEmpty()) {
			throw new IllegalArgumentException("잘못된 이미지 ID가 포함되어 있습니다: " + invalidIds);
		}

		// 삭제할 이미지: 현재 DB에 있으나 요청에서 누락된 것
		Set<Long> newImageIds = newImageDataList.stream()
			.map(ImageDto::getId)
			.filter(Objects::nonNull)
			.collect(Collectors.toSet());

		List<BoardImage> imagesToRemove = currentBoardImages.stream()
			.filter(img -> !newImageIds.contains(img.getId()))
			.toList();

		// 기존 이미지 업데이트
		for (BoardImage currentBoardImage : currentBoardImages) {
			newImageDataList.stream()
				.filter(dto -> dto.getId() != null && dto.getId().equals(currentBoardImage.getId()))
				.findFirst()
				.ifPresent(currentBoardImage::updateImage);
		}

		// 새 이미지 추가 (ID가 null인 것들)
		for (ImageDto dto : newImageDataList) {
			if (dto.getId() == null) {
				BoardImage newBoardImage = new BoardImage(board, dto.getImagePath(), dto.getName());
				board.getBoardImages().add(newBoardImage); // 연관관계 추가
			}
		}
		imageRepository.deleteAll(imagesToRemove);
	}

}
