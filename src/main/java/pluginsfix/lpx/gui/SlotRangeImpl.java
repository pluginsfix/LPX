package pluginsfix.lpx.gui;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class SlotRangeImpl implements SlotRange {
    private final PaginatedGui page;
    private final GuiInventory guiInventory;
    private final SlotDirection direction;
    private final Set<GridPosition> blockedPositions = new HashSet<>();
    private boolean initialized;
    private boolean allowOverwrite = true;
    private int row;
    private int column;

    public SlotRangeImpl(PaginatedGui page, GuiInventory guiInventory, SlotDirection direction, long ignored, int row, int column) {
        this.page = page;
        this.guiInventory = guiInventory;
        this.direction = direction;
        this.row = row;
        this.column = column;
    }

    public SlotRangeImpl(long ignored, PaginatedGui page, byte ignored2, GuiInventory guiInventory, SlotDirection direction) {
        this(page, guiInventory, direction, 0L, 0, 0);
    }

    @Override
    public Optional<GuiItem> getCurrentItem() {
        return page.getItemAt(row, column);
    }

    @Override
    public SlotRange setCurrentItem(GuiItem item) {
        if (isCurrentPositionWritable()) {
            page.setItem(row, column, item);
        }
        return this;
    }

    @Override
    public SlotRange movePreviousValid() {
        if (!initialized) {
            initialized = true;
            if (isCurrentPositionWritable() || isAtStart()) {
                return this;
            }
        }

        int maxSteps = Math.max(1, guiInventory.e9(new Object[0]) * guiInventory.e1(new Object[0]));
        for (int i = 0; i < maxSteps; i++) {
            stepBackward();
            if (isCurrentPositionWritable() || isAtStart()) {
                break;
            }
        }
        return this;
    }

    @Override
    public SlotRange moveNextValid() {
        if (!initialized) {
            initialized = true;
            if (isCurrentPositionWritable() || isAtEnd()) {
                return this;
            }
        }

        int maxSteps = Math.max(1, guiInventory.e9(new Object[0]) * guiInventory.e1(new Object[0]));
        for (int i = 0; i < maxSteps; i++) {
            stepForward();
            if (isCurrentPositionWritable() || isAtEnd()) {
                break;
            }
        }
        return this;
    }

    @Override
    public SlotRange blockPosition(int row, int column) {
        blockedPositions.add(GridPosition.lq(new Object[]{row, column}));
        return this;
    }

    @Override
    public SlotRange blockPosition(GridPosition position) {
        return blockPosition(position.RJ(new Object[0]), position.RG(new Object[0]));
    }

    @Override
    public int getRow() {
        return row;
    }

    @Override
    public SlotRange setRow(int row) {
        this.row = row;
        return this;
    }

    @Override
    public int getColumn() {
        return column;
    }

    @Override
    public SlotRange setColumn(int column) {
        this.column = column;
        return this;
    }

    @Override
    public boolean isInitialized() {
        return initialized;
    }

    @Override
    public boolean isAtEnd() {
        int maxRow = Math.max(0, guiInventory.e9(new Object[0]) - 1);
        int maxColumn = Math.max(0, guiInventory.e1(new Object[0]) - 1);
        return row == maxRow && column == maxColumn;
    }

    @Override
    public boolean isOverwriteAllowed() {
        return allowOverwrite;
    }

    @Override
    public SlotRange setOverwriteAllowed(boolean allowOverwrite) {
        this.allowOverwrite = allowOverwrite;
        return this;
    }

    private boolean isCurrentPositionWritable() {
        if (!isInBounds(row, column)) {
            return false;
        }

        GridPosition current = GridPosition.lq(new Object[]{row, column});
        if (blockedPositions.contains(current)) {
            return false;
        }

        if (!allowOverwrite && getCurrentItem().isPresent()) {
            return false;
        }

        return true;
    }

    private boolean isAtStart() {
        return row == 0 && column == 0;
    }

    private boolean isInBounds(int checkRow, int checkColumn) {
        return checkRow >= 0
            && checkColumn >= 0
            && checkRow < guiInventory.e9(new Object[0])
            && checkColumn < guiInventory.e1(new Object[0]);
    }

    private void stepForward() {
        int rows = Math.max(1, guiInventory.e9(new Object[0]));
        int columns = Math.max(1, guiInventory.e1(new Object[0]));

        if (direction == SlotDirection.VERTICAL) {
            row++;
            if (row >= rows) {
                row = 0;
                column++;
                if (column >= columns) {
                    column = 0;
                }
            }
            return;
        }

        column++;
        if (column >= columns) {
            column = 0;
            row++;
            if (row >= rows) {
                row = 0;
            }
        }
    }

    private void stepBackward() {
        int rows = Math.max(1, guiInventory.e9(new Object[0]));
        int columns = Math.max(1, guiInventory.e1(new Object[0]));

        if (direction == SlotDirection.VERTICAL) {
            row--;
            if (row < 0) {
                row = rows - 1;
                column--;
                if (column < 0) {
                    column = columns - 1;
                }
            }
            return;
        }

        column--;
        if (column < 0) {
            column = columns - 1;
            row--;
            if (row < 0) {
                row = rows - 1;
            }
        }
    }
}
