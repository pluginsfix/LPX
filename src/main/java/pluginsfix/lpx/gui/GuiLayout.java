package pluginsfix.lpx.gui;

public interface GuiLayout {
    GuiItem[] getItemsForCurrentPage();

    int getCurrentPage();

    GuiLayout setCurrentPage(int page);

    boolean isFirstPage();

    boolean isLastPage();

    GuiLayout moveToFirstPage();

    GuiLayout moveToPreviousPage();

    GuiLayout moveToNextPage();

    GuiLayout moveToLastPage();

    GuiLayout applyToRange(SlotRange range);

    GuiLayout setItems(GuiItem... items);

    GuiLayout setPageSize(int pageSize);

    default GuiItem[] a(Object[] ignored) {
        return getItemsForCurrentPage();
    }

    default int Y(Object[] ignored) {
        return getCurrentPage();
    }

    default GuiLayout R(Object[] args) {
        return setCurrentPage((Integer) args[0]);
    }

    default boolean e(Object[] ignored) {
        return isFirstPage();
    }

    default boolean L(Object[] ignored) {
        return isLastPage();
    }

    default GuiLayout y(Object[] ignored) {
        return moveToFirstPage();
    }

    default GuiLayout t(Object[] ignored) {
        return moveToPreviousPage();
    }

    default GuiLayout w(Object[] ignored) {
        return moveToNextPage();
    }

    default GuiLayout u(Object[] ignored) {
        return moveToLastPage();
    }

    default GuiLayout T(Object[] args) {
        return applyToRange((SlotRange) args[0]);
    }

    default GuiLayout n(Object... args) {
        return setItems((GuiItem[]) args[0]);
    }

    default GuiLayout q(Object[] args) {
        return setPageSize((Integer) args[0]);
    }
}
