package ostro.veda.dto;

import lombok.Data;
import lombok.experimental.Accessors;
import ostro.veda.model.ColumnType;

import java.util.ArrayList;
import java.util.List;

@Data
@Accessors(chain = true)
public class BoardColumnDto {

    private long columnId;
    private String columnName;
    private ColumnType columnType;
    private int columnIndex;
    private List<CardDto> cards = new ArrayList<>();
}
