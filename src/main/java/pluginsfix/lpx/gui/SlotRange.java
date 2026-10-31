package pluginsfix.lpx.gui;

import java.util.Optional;

public interface SlotRange {
    Optional<GuiItem> getCurrentItem();

    SlotRange setCurrentItem(GuiItem item);

    SlotRange movePreviousValid();

    SlotRange moveNextValid();

    SlotRange blockPosition(int row, int column);

    SlotRange blockPosition(GridPosition position);

    int getRow();

    SlotRange setRow(int row);

    int getColumn();

    SlotRange setColumn(int column);

    boolean isInitialized();

    boolean isAtEnd();

    boolean isOverwriteAllowed();

    SlotRange setOverwriteAllowed(boolean allowOverwrite);

    default Optional<GuiItem> J(Object[] ignored) {
        return getCurrentItem();
    }

    default SlotRange K(Object[] args) {
        return setCurrentItem((GuiItem) args[0]);
    }

    default SlotRange X(Object[] ignored) {
        return movePreviousValid();
    }

    default SlotRange z(Object[] ignored) {
        return moveNextValid();
    }

    default SlotRange B(Object[] args) {
        return blockPosition((Integer) args[0], (Integer) args[1]);
    }

    default SlotRange f(Object[] args) {
        return blockPosition((GridPosition) args[0]);
    }

    default int s(Object[] ignored) {
        return getRow();
    }

    default SlotRange E(Object[] args) {
        return setRow((Integer) args[0]);
    }

    default int v(Object[] ignored) {
        return getColumn();
    }

    default SlotRange g(Object[] args) {
        return setColumn((Integer) args[0]);
    }

    default boolean A(Object[] ignored) {
        return isInitialized();
    }

    default boolean H(Object[] ignored) {
        return isAtEnd();
    }

    default boolean I(Object[] ignored) {
        return isOverwriteAllowed();
    }

    default SlotRange b(Object[] args) {
        return setOverwriteAllowed((Boolean) args[0]);
    }
}
