package kernel.maidlab.admin.board.repository.support;

import org.springframework.data.jpa.repository.JpaRepository;

import kernel.maidlab.domain.board.entity.Answer;

public interface AdminAnswerRepository extends JpaRepository<Answer, Long> {

}
