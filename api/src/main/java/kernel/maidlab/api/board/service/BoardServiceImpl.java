package kernel.maidlab.api.board.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import kernel.maidlab.api.auth.entity.Consumer;
import kernel.maidlab.api.auth.entity.Manager;
import kernel.maidlab.api.auth.jwt.JwtFilter;
import kernel.maidlab.api.board.common.UserBase;
import kernel.maidlab.api.board.dto.BoardQueryDto;
import kernel.maidlab.api.board.dto.ImageDto;
import kernel.maidlab.api.board.dto.request.BoardUpdateRequestDto;
import kernel.maidlab.api.board.dto.request.BoardRequestDto;
import kernel.maidlab.api.board.dto.response.BoardDetailResponseDto;
import kernel.maidlab.api.board.dto.response.BoardResponseDto;
import kernel.maidlab.api.board.entity.Board;
import kernel.maidlab.api.board.entity.Image;
import kernel.maidlab.api.board.repository.BoardRepository;
import kernel.maidlab.api.board.repository.ImageRepository;
import kernel.maidlab.api.util.AuthUtil;
import kernel.maidlab.common.enums.UserType;
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
    private final AuthUtil authUtil;

    // 게시판 글 생성
    public void createConsumerBoard(
            HttpServletRequest request,
            BoardRequestDto boardRequestDto) {


        // todo: 사용자에 따른 게시판 생성 로직 리팩토링 필요
        // 현재 수요자 기준으로 게시핀이 생성됨
        Consumer consumer = authUtil.getConsumer(request);
        Board board = Board.createConsumerBoard(consumer, boardRequestDto);
        boardRepository.save(board);

        boardRequestDto.getImages()
                .forEach((imageDto) -> imageRepository.save(
                        new Image(
                                board,
                                imageDto.getImagePath(),
                                imageDto.getName())));
    }

    // 게시글 전체 조회
    @Transactional(readOnly = true)
    public List<BoardResponseDto> getConsumerBoardList(HttpServletRequest request) {

        Object user = request.getAttribute(JwtFilter.CURRENT_USER_KEY);
        UserType userType = (UserType) request.getAttribute(JwtFilter.CURRENT_USER_TYPE_KEY);

        List<BoardQueryDto> boardQueryDtoList = getBoardQueryDtoList(user, userType);

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

        // todo: 사용자에 따른 상세 접근 필요
        // 검증 로직
        Consumer consumer = authUtil.getConsumer(request);
        Board board = boardRepository.findByIdAndIsDeletedFalse(boardId)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 게시물 입니다."));

        // 토큰으로 찾은 수요자id와 PathVariable로 넘어온 게시판id로 찾은 consumerId와 비교
        if (!consumer.getId().equals(board.getConsumer().getId())) {
            throw new AccessDeniedException("해당 게시글에 접근할 권한이 없습니다.");
        }

        // 답변여부가 true면 답변까지 조회
        if (board.getIsAnswered()) {
            board = boardRepository.findBoardWithAnswerIfAnswered(boardId)
                    .orElseThrow(() -> new EntityNotFoundException("답변이 존재하지 않습니다."));
        }

        List<Image> images = imageRepository.findAllByBoardId(boardId);

        return BoardDetailResponseDto.from(board, images);

    }

    // 수정
    public void modifyBoard(
            HttpServletRequest request,
            Long boardId,
            BoardUpdateRequestDto boardUpdateRequestDto) {

        Board board = boardRepository.findByIdAndIsDeletedFalse(boardId)
                .orElseThrow(() -> new RuntimeException("게시글이 존재하지 않습니다."));

        // 사용자 검증
        UserBase user = getUser(request);
        if (!isUserBoardWriter(board, user)) {
            throw new RuntimeException("수정 권한이 없습니다.");
        }

        board.boardUpdate(boardUpdateRequestDto);

        List<Image> currentImages = imageRepository.findAllByBoardId(boardId);
        List<ImageDto> newImageDataList = boardUpdateRequestDto.getImages();

        updateImages(currentImages, newImageDataList, board);

    }


    // 게시글 삭제
    public void deleteBoard(
            HttpServletRequest request,
            Long boardId
    ){
        Board board = boardRepository
                .findByIdAndIsDeletedFalse(boardId)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 게시판 입니다."));

        board.updateIsDelete(true);
    }


    /**
     *서비스 외의 로직
     */
    // 사용자 접근 검증
    public boolean isUserBoardWriter(Board board, UserBase user) {
        if (user instanceof Consumer consumer) {
            return board.getConsumer() != null && board.getConsumer().getId().equals(consumer.getId());
        } else if (user instanceof Manager manager) {
            return board.getManager() != null && board.getManager().getId().equals(manager.getId());
        }
        return false;
    }


    // user타입에 따른 board 조회
    public List<BoardQueryDto> getBoardQueryDtoList (Object user, UserType userType) {

        if (userType == UserType.CONSUMER) {
            Consumer consumer = (Consumer) user;
            return boardRepository.findAllByUserIdIsDeletedFalse(consumer.getId(), userType);

        } else if (userType == UserType.MANAGER) {
            Manager manager = (Manager) user;
            return boardRepository.findAllByUserIdIsDeletedFalse(manager.getId(), userType);

        }
        throw new IllegalArgumentException("유효하지 않은 사용자 타입입니다: " + userType);
    }

    // 유저 찾기
    public UserBase getUser(HttpServletRequest request) {

        UserType userType = (UserType) request.getAttribute(JwtFilter.CURRENT_USER_TYPE_KEY);
        Object user = request.getAttribute(JwtFilter.CURRENT_USER_KEY);

        return switch (userType.getName()){
            case "회원" -> (Consumer) user;
            case "매니저" -> (Manager) user;
            default -> throw new IllegalArgumentException("유효하지 않은 사용자 유형 입니다.");
        };
    }


    // 이미지 수정 로직
    // todo:너무 많은 역할을 담담하고 있음 - 추후 리펙토링 필요
    public void updateImages(List<Image> currentImages, List<ImageDto> newImageDataList, Board board) {

        // 사용자가 images null을 보낸 경우 기존 이미지만 삭제후 리턴
        if(newImageDataList == null){
            imageRepository.deleteAllByBoard(board);
            board.getImages().clear();
            return;
        }

        // 현재 이미지 ID 목록
        Set<Long> currentImageIds = currentImages.stream()
                .map(Image::getId)
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

        List<Image> imagesToRemove = currentImages.stream()
                .filter(img -> !newImageIds.contains(img.getId()))
                .toList();

        // 기존 이미지 업데이트
        for (Image currentImage : currentImages) {
            newImageDataList.stream()
                    .filter(dto -> dto.getId() != null && dto.getId().equals(currentImage.getId()))
                    .findFirst()
                    .ifPresent(currentImage::updateImage);
        }

        // 새 이미지 추가 (ID가 null인 것들)
        for (ImageDto dto : newImageDataList) {
            if (dto.getId() == null) {
                Image newImage = new Image(board, dto.getName(), dto.getImagePath());
                board.getImages().add(newImage); // 연관관계 추가
            }
        }
        imageRepository.deleteAll(imagesToRemove);
    }

}// end class
