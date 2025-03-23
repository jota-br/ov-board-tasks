package ostro.veda.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
@Entity(name = "tb_column")
public class BoardColumn implements Comparable<BoardColumn> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "column_id")
    private long columnId;

    private String columnName;
    private ColumnType columnType;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Card> cards;

    private int columnIndex;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        BoardColumn that = (BoardColumn) o;
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
    public int compareTo(BoardColumn o) {
        return this.columnType.compareTo(o.getColumnType());
    }
}
