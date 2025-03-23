package ostro.veda;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import ostro.veda.dto.BoardColumnDto;
import ostro.veda.dto.BoardDto;
import ostro.veda.dto.CardDto;
import ostro.veda.model.ColumnType;
import ostro.veda.service.BoardServiceImpl;

import java.util.Scanner;

@Component
public class Start implements CommandLineRunner {

    private boolean flag = true;
    private long selectedBoard;
    private final Scanner scanner = new Scanner(System.in);
    private final BoardServiceImpl boardService;

    @Autowired
    public Start(BoardServiceImpl boardService) {
        this.boardService = boardService;
    }

    @Override
    public void run(String... args) throws Exception {

        while (this.flag) {
            System.out.println(getCommands());
            String input = scanner.nextLine();
            processInput(input);
        }
    }

    private String getCommands() {
        return """
                Select operation:
                \t1- Select board
                \t2- Create board
                \t3- Create column
                \t4- Create card
                \t5- Move card to next column
                \t6- Block card
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
            case "6" -> blockCard();
            case "7" -> cancelCard();
            case "8" -> closeBoard();
            case "9" -> System.out.println(boardService.getBoards());
            default -> closeApplication();
        }
    }

    private void selectBoard() {
        System.out.println("Select board by id:");
        this.selectedBoard = Long.parseLong(scanner.nextLine());
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
        BoardColumnDto boardColumnDto = new BoardColumnDto()
                .setColumnType(ColumnType.CUSTOM)
                .setColumnName(columnName);
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

    private void closeApplication() {
        scanner.close();
        this.flag = false;
    }
}
