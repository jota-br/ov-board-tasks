package ostro.veda.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

@Data
@Accessors(chain = true)
public class BoardDto {

    private long boardId;
    private String boardName;
    private List<BoardColumnDto> boardColumnDtoList = new ArrayList<>();
    private boolean isClosed;
}
