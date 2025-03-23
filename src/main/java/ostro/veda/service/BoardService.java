package ostro.veda.service;

import ostro.veda.dto.BoardColumnDto;
import ostro.veda.dto.BoardDto;
import ostro.veda.dto.CardDto;

import java.util.List;

public interface BoardService {

    void createBoard(BoardDto boardDto);

    BoardDto getBoardById(long id);

    List<BoardDto> getBoards();

    void addColumn(long boardId, BoardColumnDto boardColumnDto);

    void addCard(long boardId, CardDto cardDto);
}
