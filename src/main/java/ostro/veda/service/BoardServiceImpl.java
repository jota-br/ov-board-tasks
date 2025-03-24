package ostro.veda.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ostro.veda.dto.BoardColumnDto;
import ostro.veda.dto.BoardDto;
import ostro.veda.dto.CardDto;
import ostro.veda.model.Board;
import ostro.veda.model.BoardColumn;
import ostro.veda.model.Card;
import ostro.veda.model.ColumnType;
import ostro.veda.repository.BoardRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BoardServiceImpl implements BoardService {

    private final BoardRepository boardRepository;

    @Autowired
    public BoardServiceImpl(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }

    @Override
    public BoardDto findById(long boardId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new EntityNotFoundException("Entity not found"));
        return toDto(board);
    }

    @Override
    public void createBoard(BoardDto boardDto) {
        Board board = buildBoard(boardDto);
        boardRepository.save(board);
    }

    @Override
    public BoardDto getBoardById(long id) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Entity not found"));
//        Collections.sort(board.getBoardColumns());
        return toDto(board);
    }

    @Override
    public List<BoardDto> findAllBoards() {
        return boardRepository.findAll()
                .stream()
                .map(this::toDto)
                .toList();
    }

    @Override
    public void addColumn(long boardId, BoardColumnDto boardColumnDto) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new EntityNotFoundException("Entity not found"));

        defineColumnIndex(boardColumnDto.getColumnIndex(), board.getBoardColumns());

        board.getBoardColumns()
                .add(
                        new BoardColumn()
                                .setColumnId(boardColumnDto.getColumnId())
                                .setColumnName(boardColumnDto.getColumnName())
                                .setColumnIndex(boardColumnDto.getColumnIndex())
                                .setColumnType(boardColumnDto.getColumnType())
                );

        boardRepository.save(board);
    }

    @Override
    public void addCard(long boardId, CardDto cardDto) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new EntityNotFoundException("Entity not found"));

        BoardColumn boardColumn = board.getBoardColumns()
                .stream()
                .filter(column -> column.getColumnType().equals(ColumnType.START))
                .findFirst()
                .orElseThrow(
                        () -> new EntityNotFoundException("Required START column not found in selected Board")
                );

        boardColumn.getCards()
                .add(new Card()
                        .setName(cardDto.getName())
                        .setDescription(cardDto.getDescription())
                        .setBlocked(cardDto.isBlocked())
                        .setBlockedAt(cardDto.getBlockedAt())
                        .setBlockedDescription(cardDto.getBlockedDescription())
                        .setCreatedAt(cardDto.getCreatedAt()));

        boardRepository.save(board);
    }

    @Override
    public void moveCard(long boardId, long cardId, boolean forCancellation) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new EntityNotFoundException("Entity not found"));

        for (BoardColumn boardColumn : board.getBoardColumns()) {
            Card card = boardColumn.getCards().stream()
                    .filter(c -> c.getCardId() == cardId)
                    .findFirst()
                    .orElse(null);
            if (card != null) {

                String message = null;
                if (
                        boardColumn.getColumnType().equals(ColumnType.FINAL) ||
                                boardColumn.getColumnType().equals(ColumnType.CANCELED)
                )
                    message = "Cards in FINAL and CANCELED column's cannot be moved";
                else if (card.isBlocked())
                    message = "Card is blocked and cannot be moved";

                if (message != null)
                    throw new IllegalStateException(message);

                boardColumn.getCards().remove(card);
                int index = 0;
                if (forCancellation)
                    index = board.getBoardColumns().size() - 1;
                else
                    index = board.getBoardColumns().indexOf(boardColumn);

                board.getBoardColumns().get(++index).getCards().add(card);

                boardRepository.save(board);
                return;
            }
        }
        throw new EntityNotFoundException("Entity not found");
    }

    @Override
    public void closeBoard(long boardId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new EntityNotFoundException("Entity not found"));

        board.setClosed(true);
    }

    private Board buildBoard(BoardDto boardDto) {
        List<BoardColumn> boardColumnList = new ArrayList<>(
                List.of(new BoardColumn()
                                .setColumnIndex(0)
                                .setColumnType(ColumnType.START)
                                .setColumnName(ColumnType.START.toString()),
                        new BoardColumn()
                                .setColumnIndex(1)
                                .setColumnType(ColumnType.FINAL)
                                .setColumnName(ColumnType.FINAL.toString()),
                        new BoardColumn()
                                .setColumnIndex(2)
                                .setColumnType(ColumnType.CANCELED)
                                .setColumnName(ColumnType.CANCELED.toString())));

        return new Board()
                .setBoardId(boardDto.getBoardId())
                .setBoardName(boardDto.getBoardName())
                .setBoardColumns(boardColumnList);
    }

    private void defineColumnIndex(int columnIndex, List<BoardColumn> boardColumnList) {
        for (BoardColumn boardColumn : boardColumnList) {
            int currentColumnIndex = boardColumn.getColumnIndex();
            if (boardColumn.getColumnIndex() >= columnIndex) boardColumn.setColumnIndex(++currentColumnIndex);
        }
    }

    private BoardDto toDto(Board board) {
        return new BoardDto()
                .setBoardId(board.getBoardId())
                .setBoardName(board.getBoardName())
                .setClosed(board.isClosed())
                .setBoardColumnDtoList(
                        board.getBoardColumns().stream()
                                .map(c ->
                                        new BoardColumnDto()
                                                .setColumnIndex(c.getColumnIndex())
                                                .setColumnType(c.getColumnType())
                                                .setColumnName(c.getColumnName())
                                                .setCards(c.getCards().stream()
                                                        .map(card -> new CardDto()
                                                                .setCardId(card.getCardId())
                                                                .setName(card.getName())
                                                                .setDescription(card.getDescription())
                                                                .setBlocked(card.isBlocked())
                                                                .setBlockedDescription(card.getBlockedDescription())
                                                                .setBlockedAt(card.getBlockedAt())
                                                                .setCreatedAt(card.getCreatedAt())
                                                        )
                                                        .collect(Collectors.toList())
                                                ))
                                .sorted()
                                .collect(Collectors.toList())
                );
    }
}
