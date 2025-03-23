package ostro.veda.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ostro.veda.model.BoardColumn;

public interface BoardColumnRepository extends JpaRepository<BoardColumn, Long> {
}
