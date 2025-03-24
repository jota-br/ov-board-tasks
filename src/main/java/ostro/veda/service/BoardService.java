package ostro.veda.service;

import ostro.veda.dto.BoardColumnDto;
import ostro.veda.dto.BoardDto;
import ostro.veda.dto.CardDto;

import java.util.List;

public interface BoardService {

    BoardDto findById(long boardId);

    void createBoard(BoardDto boardDto);

    BoardDto getBoardById(long id);

    List<BoardDto> findAllBoards();

    void addColumn(long boardId, BoardColumnDto boardColumnDto);

    void addCard(long boardId, CardDto cardDto);

    void moveCard(long boardId, long cardId, boolean forCancellation);

    void closeBoard(long boardId);
}
