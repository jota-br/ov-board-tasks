package ostro.veda.dto;

import lombok.Data;
import lombok.experimental.Accessors;
import ostro.veda.model.ColumnType;

import java.util.ArrayList;
import java.util.List;

@Data
@Accessors(chain = true)
public class BoardColumnDto implements Comparable<BoardColumnDto> {

    private long columnId;
    private String columnName;
    private ColumnType columnType;
    private int columnIndex;
    private List<CardDto> cards = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        BoardColumnDto that = (BoardColumnDto) o;
        if (columnType.equals(ColumnType.START) || columnType.equals(ColumnType.CANCELED) || columnType.equals(ColumnType.FINAL)) {
            return columnType.ordinal() == that.columnType.ordinal();
        }
        return columnIndex == that.columnIndex && columnType.ordinal() == that.columnType.ordinal();
    }

    @Override
    public int hashCode() {
        int result = columnType.hashCode();
        result = 31 * result + columnIndex;
        return result;
    }

    @Override
    public int compareTo(BoardColumnDto o) {
        int result = this.columnType.compareTo(o.getColumnType());
        if (result == 0)
            return Integer.compare(this.columnIndex, o.columnIndex);
        return result;
    }

    @Override
    public String toString() {
        return "BoardColumnDto{" +
                "columnId=" + columnId +
                ", columnName='" + columnName + '\'' +
                ", columnType=" + columnType +
                ", columnIndex=" + columnIndex +
                ", cards=" + cards +
                '}' + "\n";
    }
}
