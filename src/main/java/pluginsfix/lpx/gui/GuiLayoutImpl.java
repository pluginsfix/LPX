package pluginsfix.lpx.gui;

import java.util.Arrays;

public class GuiLayoutImpl implements GuiLayout {
    private int currentPage;
    private GuiItem[] items;
    private int pageSize;

    public GuiLayoutImpl(long ignored) {
        this.items = new GuiItem[0];
        this.pageSize = 45;
        this.currentPage = 0;
    }

    @Override
    public GuiItem[] getItemsForCurrentPage() {
        int start = Math.max(0, currentPage) * Math.max(1, pageSize);
        if (start >= items.length) {
            return new GuiItem[0];
        }
        int end = Math.min(items.length, start + Math.max(1, pageSize));
        return Arrays.copyOfRange(items, start, end);
    }

    @Override
    public int getCurrentPage() {
        return currentPage;
    }

    @Override
    public GuiLayout setCurrentPage(int page) {
        this.currentPage = Math.max(0, page);
        return this;
    }

    @Override
    public boolean isFirstPage() {
        return currentPage == 0;
    }

    @Override
    public boolean isLastPage() {
        int safePageSize = Math.max(1, pageSize);
        int pages = (int) Math.ceil(items.length / (double) safePageSize);
        int lastPage = Math.max(0, pages - 1);
        return currentPage >= lastPage;
    }

    @Override
    public GuiLayout moveToFirstPage() {
        this.currentPage = 0;
        return this;
    }

    @Override
    public GuiLayout moveToPreviousPage() {
        if (!isFirstPage()) {
            this.currentPage--;
        }
        return this;
    }

    @Override
    public GuiLayout moveToNextPage() {
        if (!isLastPage()) {
            this.currentPage++;
        }
        return this;
    }

    @Override
    public GuiLayout moveToLastPage() {
        int safePageSize = Math.max(1, pageSize);
        int pages = (int) Math.ceil(items.length / (double) safePageSize);
        this.currentPage = Math.max(0, pages - 1);
        return this;
    }

    @Override
    public GuiLayout applyToRange(SlotRange range) {
        GuiItem[] pageItems = getItemsForCurrentPage();
        for (GuiItem item : pageItems) {
            range.moveNextValid().setCurrentItem(item);
            if (range.isAtEnd()) {
                break;
            }
        }
        return this;
    }

    @Override
    public GuiLayout setItems(GuiItem... items) {
        this.items = items;
        return this;
    }

    @Override
    public GuiLayout setPageSize(int pageSize) {
        this.pageSize = Math.max(1, pageSize);
        return this;
    }
}
