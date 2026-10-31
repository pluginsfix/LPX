package pluginsfix.lpx.gui;
import java.util.Objects;
public final class GridPosition {
    private final int row;
    private final int column;
    public GridPosition(int row, int column) {
        this.row = row;
        this.column = column;
    }
    public static GridPosition of(Object[] args) {
        int row = (Integer) args[0];
        int column = (Integer) args[1];
        return new GridPosition(row, column);
    }
    public int getRow() {
        return row;
    }
    public int getColumn() {
        return column;
    }
    public int RJ(Object[] ignored) {
        return getRow();
    }
    public int RG(Object[] ignored) {
        return getColumn();
    }
    public static GridPosition lq(Object[] args) {
        return of(args);
    }
    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof GridPosition that)) {
            return false;
        }
        return row == that.row && column == that.column;
    }
    @Override
    public int hashCode() {
        return Objects.hash(row, column);
    }
}
