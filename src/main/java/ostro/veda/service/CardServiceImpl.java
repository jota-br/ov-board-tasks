package ostro.veda.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ostro.veda.model.Card;
import ostro.veda.repository.CardRepository;

import java.time.LocalDateTime;

@Service
public class CardServiceImpl implements CardService {

    private final CardRepository cardRepository;

    @Autowired
    public CardServiceImpl(CardRepository cardRepository) {
        this.cardRepository = cardRepository;
    }

    @Override
    public void blockUnblockCard(long cardId, String blockedDescription) {
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new EntityNotFoundException("Entity not found"));

        card
                .setBlockedAt(LocalDateTime.now())
                .setBlocked(!card.isBlocked())
                .setBlockedDescription(blockedDescription);

        cardRepository.save(card);
    }
}
