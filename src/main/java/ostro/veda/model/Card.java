package ostro.veda.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
@Entity(name = "tb_card")
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "card_id")
    private long cardId;

    private String name;
    private String description;

    private boolean isBlocked;
    private String blockedDescription;
    private LocalDateTime blockedAt;

    @CreationTimestamp
    private LocalDateTime createdAt;
}
