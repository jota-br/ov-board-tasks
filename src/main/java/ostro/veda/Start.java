package ostro.veda;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import ostro.veda.dto.BoardColumnDto;
import ostro.veda.dto.BoardDto;
import ostro.veda.dto.CardDto;
import ostro.veda.model.ColumnType;
import ostro.veda.service.BoardServiceImpl;
import ostro.veda.service.CardServiceImpl;

import java.util.Scanner;

@Slf4j
@Component
public class Start implements CommandLineRunner {

    private boolean flag = true;
    private long selectedBoard;
    private final Scanner scanner = new Scanner(System.in);

    private final BoardServiceImpl boardService;
    private final CardServiceImpl cardService;

    @Autowired
    public Start(BoardServiceImpl boardService, CardServiceImpl cardService) {
        this.boardService = boardService;
        this.cardService = cardService;
    }

    @Override
    public void run(String... args) {

        try {
            while (this.flag) {
                System.out.println(getCommands());
                String input = scanner.nextLine();
                processInput(input);
            }
        } catch (Exception e) {
            log.warn(e.getMessage());
        }
    }

    private String getCommands() {
        if (this.selectedBoard == 0)
            return """
                    Select operation:
                    \t1- Select board
                    \t2- Create board
                    \t10- Close application
                    """;
        else
            return """
                    Select operation:
                    \t3- Create column
                    \t4- Create card
                    \t5- Move card to next column
                    \t6- Block / Unblock card
                    \t7- Cancel card
                    \t8- Close board
                    \t9- Get all Boards
                    \t10- Close application
                    """;
    }

    private void processInput(String input) {
        switch (input) {
            case "1" -> selectBoard();
            case "2" -> createBoard();
            case "3" -> createColumn();
            case "4" -> createCard();
            case "5" -> moveCard();
            case "6" -> blockUnblockCard();
            case "7" -> cancelCard();
            case "8" -> closeBoard();
            case "9" -> System.out.println(boardService.findAllBoards());
            default -> closeApplication();
        }
    }

    private void selectBoard() {
        System.out.println("Select board by id:");
        long boardId = Long.parseLong(scanner.nextLine());
        BoardDto boardDto = boardService.findById(boardId);

        if (boardDto.isClosed()) throw new IllegalStateException("Desired board cannot be selected, it is closed");
        this.selectedBoard = boardId;
    }

    private void createBoard() {
        System.out.println("Write Board name:");
        String boardName = scanner.nextLine();
        BoardDto boardDto = new BoardDto();
        boardDto.setBoardName(boardName);
        boardService.createBoard(boardDto);
    }

    private void createColumn() {
        System.out.println("Write column name");
        String columnName = scanner.nextLine();
        System.out.println("Write column index");
        int columnIndex = Integer.parseInt(scanner.nextLine());

        BoardColumnDto boardColumnDto = new BoardColumnDto()
                .setColumnType(ColumnType.CUSTOM)
                .setColumnName(columnName)
                .setColumnIndex(columnIndex);
        boardService.addColumn(this.selectedBoard, boardColumnDto);
    }

    private void createCard() {
        System.out.println("Write card name");
        String cardName = scanner.nextLine();

        System.out.println("Write card description");
        String cardDescription = scanner.nextLine();

        CardDto cardDto = new CardDto()
                .setName(cardName)
                .setDescription(cardDescription);

        boardService.addCard(this.selectedBoard, cardDto);
    }

    private void moveCard() {
        System.out.println("Write card id to move to next column");
        long cardId = Long.parseLong(scanner.nextLine());
        boardService.moveCard(this.selectedBoard, cardId, false);
    }

    private void blockUnblockCard() {
        System.out.println("Write card id to block / unblock");
        long cardId = Long.parseLong(scanner.nextLine());
        System.out.println("Write block / unblock description");
        String blockedDescription = scanner.nextLine();

        cardService.blockUnblockCard(cardId, blockedDescription);
    }

    private void cancelCard() {
        System.out.println("Write card id to cancel");
        long cardId = Long.parseLong(scanner.nextLine());

        boardService.moveCard(this.selectedBoard, cardId, true);
    }

    private void closeBoard() {
        boardService.closeBoard(this.selectedBoard);
    }

    private void closeApplication() {
        scanner.close();
        this.flag = false;
    }
}
