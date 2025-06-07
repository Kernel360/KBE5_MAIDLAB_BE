package kernel.maidlab.api.board.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import kernel.maidlab.api.board.dto.request.BoardRequestDto;
import kernel.maidlab.api.board.dto.request.BoardUpdateRequestDto;
import kernel.maidlab.api.board.dto.response.BoardDetailResponseDto;
import kernel.maidlab.api.board.dto.response.BoardResponseDto;
import kernel.maidlab.common.dto.ResponseDto;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Tag(name = "Board API", description = "게시판 관련 API 명세입니다.")
public interface BoardApi {

    @Operation(summary = "게시글 작성", description = "수요자가 게시글을 작성합니다.")
    ResponseEntity<ResponseDto<String>> createBoard(
            @Parameter(hidden = true) HttpServletRequest request,
            BoardRequestDto boardRequestDto
    );

    @Operation(summary = "전체 게시글 조회", description = "수요자가 작성한 전체 게시글을 조회합니다.")
    ResponseEntity<ResponseDto<List<BoardResponseDto>>> getConsumerBoardList(
            @Parameter(hidden = true) HttpServletRequest request
    );

    @Operation(summary = "게시글 상세 조회", description = "특정 게시글의 상세 정보를 조회합니다.")
    ResponseEntity<ResponseDto<BoardDetailResponseDto>> getConsumerBoard(
            @Parameter(hidden = true) HttpServletRequest request,
            @Parameter(description = "게시글 ID") Long boardId
    );

    @Operation(summary = "게시글 수정", description = "게시글을 수정합니다.")
    ResponseEntity<ResponseDto<Object>> updateBoard(
            @Parameter(hidden = true) HttpServletRequest request,
            @Parameter(description = "게시글 ID") Long boardId,
            BoardUpdateRequestDto boardUpdateRequestDto
    );

    @Operation(summary = "게시글 삭제", description = "게시글을 삭제합니다.")
    ResponseEntity<ResponseDto<Object>> deleteBoard(
            @Parameter(hidden = true) HttpServletRequest request,
            @Parameter(description = "게시글 ID") Long boardId
    );
}