package ostro.veda.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ostro.veda.model.Card;

public interface CardRepository extends JpaRepository<Card, Long> {

}
