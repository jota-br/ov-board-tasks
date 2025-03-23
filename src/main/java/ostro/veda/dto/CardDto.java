package ostro.veda.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
public class CardDto {

    private long cardId;
    private String name;
    private String description;
    private boolean isBlocked;
    private String blockedDescription;
    private LocalDateTime blockedAt;
    private LocalDateTime createdAt;
}
