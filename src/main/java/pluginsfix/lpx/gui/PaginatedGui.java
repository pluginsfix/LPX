package pluginsfix.lpx.gui;

import java.util.Optional;

public interface PaginatedGui {
    GuiInventory getGuiInventory();

    GuiLayout getLayout();

    Optional<SlotRange> findRange(String id);

    SlotRange createRange(String id, SlotDirection direction, int row, int col);

    SlotRange createRange(String id, SlotDirection direction, GridPosition position);

    SlotRange createTemporaryRange(SlotDirection direction, int row, int col);

    SlotRange createTemporaryRange(SlotDirection direction, GridPosition position);

    GuiItem[][] getItemsGrid();

    Optional<GuiItem> getItemAt(int row, int col);

    Optional<GuiItem> getItemAt(GridPosition position);

    Optional<GuiItem> getItemBySlot(int slot);

    PaginatedGui setItem(int row, int col, GuiItem item);

    PaginatedGui setItem(GridPosition position, GuiItem item);

    PaginatedGui fillFirstEmpty(GuiItem item);

    PaginatedGui applyLayout();

    PaginatedGui fillRow(int row, GuiItem item);

    PaginatedGui fillColumn(int col, GuiItem item);

    PaginatedGui fillAll(GuiItem item);

    PaginatedGui fillRectangle(int startRow, int startCol, int endRow, int endCol, GuiItem item);

    PaginatedGui fillRectangle(GridPosition from, GridPosition to, GuiItem item);

    Object getMetadata(String key);

    Object getMetadataOrDefault(String key, Object fallback);

    PaginatedGui setMetadata(String key, Object value);

    default GuiInventory F(Object[] ignored) {
        return getGuiInventory();
    }

    default GuiLayout h(Object[] ignored) {
        return getLayout();
    }

    default Optional<SlotRange> j(Object[] args) {
        return findRange((String) args[0]);
    }

    default SlotRange k(Object[] args) {
        return createRange((String) args[0], (SlotDirection) args[1], (Integer) args[2], (Integer) args[3]);
    }

    default SlotRange S(Object[] args) {
        return createTemporaryRange((SlotDirection) args[0], (Integer) args[2], (Integer) args[3]);
    }

    default SlotRange U(Object[] args) {
        return createRange((String) args[0], (SlotDirection) args[2], (GridPosition) args[3]);
    }

    default SlotRange Q(Object[] args) {
        return createTemporaryRange((SlotDirection) args[1], (GridPosition) args[2]);
    }

    default GuiItem[][] C(Object[] ignored) {
        return getItemsGrid();
    }

    default Optional<GuiItem> M(Object[] args) {
        if (args.length == 1 && args[0] instanceof GridPosition) {
            return getItemAt((GridPosition) args[0]);
        }
        if (args.length >= 2 && args[0] instanceof Integer && args[1] instanceof Integer) {
            return getItemAt((Integer) args[0], (Integer) args[1]);
        }
        if (args.length == 1 && args[0] instanceof Integer) {
            return getItemBySlot((Integer) args[0]);
        }
        return Optional.empty();
    }

    default Optional<GuiItem> o(Object[] args) {
        return getItemAt((Integer) args[0], (Integer) args[1]);
    }

    default Optional<GuiItem> V(Object[] args) {
        return getItemAt((GridPosition) args[1]);
    }

    default PaginatedGui x(Object[] args) {
        return setItem((Integer) args[0], (Integer) args[1], (GuiItem) args[2]);
    }

    default PaginatedGui O(Object[] args) {
        return setItem((GridPosition) args[0], (GuiItem) args[1]);
    }

    default PaginatedGui l(Object[] args) {
        return fillFirstEmpty((GuiItem) args[1]);
    }

    default PaginatedGui P(Object[] args) {
        return applyLayout();
    }

    default PaginatedGui i(Object[] args) {
        return fillRow((Integer) args[0], (GuiItem) args[2]);
    }

    default PaginatedGui m(Object[] args) {
        return fillColumn((Integer) args[1], (GuiItem) args[2]);
    }

    default PaginatedGui N(Object[] args) {
        return fillAll((GuiItem) args[1]);
    }

    default PaginatedGui Z(Object[] args) {
        return fillRectangle((Integer) args[1], (Integer) args[2], (Integer) args[3], (Integer) args[4], (GuiItem) args[5]);
    }

    default PaginatedGui p(Object[] args) {
        return fillRectangle((GridPosition) args[0], (GridPosition) args[2], (GuiItem) args[3]);
    }

    default Object G(Object[] args) {
        return getMetadata((String) args[0]);
    }

    default Object d(Object[] args) {
        return getMetadataOrDefault((String) args[1], args[2]);
    }

    default PaginatedGui D(Object[] args) {
        return setMetadata((String) args[0], args[1]);
    }
}
