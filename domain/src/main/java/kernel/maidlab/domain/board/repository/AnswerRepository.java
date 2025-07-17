package kernel.maidlab.domain.board.repository;

import kernel.maidlab.domain.board.entity.Answer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnswerRepository extends JpaRepository<Answer, Long> {

}
