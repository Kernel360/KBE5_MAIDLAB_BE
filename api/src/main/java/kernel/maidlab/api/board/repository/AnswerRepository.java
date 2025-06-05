package kernel.maidlab.api.board.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import kernel.maidlab.api.board.entity.Answer;

public interface AnswerRepository extends JpaRepository<Answer, Long> {

}
