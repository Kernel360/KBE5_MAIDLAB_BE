package kernel.maidlab.api.board.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import kernel.maidlab.common.entity.board.Answer;

public interface AnswerRepository extends JpaRepository<Answer, Long> {

}
