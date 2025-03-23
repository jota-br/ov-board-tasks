package ostro.veda.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ostro.veda.model.Board;

public interface BoardRepository extends JpaRepository<Board, Long> {
}
