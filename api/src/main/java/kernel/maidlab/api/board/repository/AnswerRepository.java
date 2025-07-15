package kernel.maidlab.api.board.repository;

import kernel.maidlab.api.board.entity.Answer;
import org.springframework.data.jpa.repository.JpaRepository;


public interface AnswerRepository extends JpaRepository<Answer, Long> {

}
