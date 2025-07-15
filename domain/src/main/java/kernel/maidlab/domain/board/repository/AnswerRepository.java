package kernel.maidlab.domain.board.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import kernel.maidlab.domain.board.entity.Answer;

public interface AnswerRepository extends JpaRepository<Answer, Long> {

}
