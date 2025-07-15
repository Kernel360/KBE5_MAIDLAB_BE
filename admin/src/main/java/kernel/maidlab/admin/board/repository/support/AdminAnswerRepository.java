package kernel.maidlab.admin.board.repository.support;

import kernel.maidlab.api.board.entity.Answer;
import org.springframework.data.jpa.repository.JpaRepository;


public interface AdminAnswerRepository extends JpaRepository<Answer, Long> {

}
