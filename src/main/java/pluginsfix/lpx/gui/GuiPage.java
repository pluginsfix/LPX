package pluginsfix.lpx.gui;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class GuiPage implements PaginatedGui {
    private final GuiInventory guiInventory;
    private final Player viewer;
    private final GuiItem[][] grid;
    private final GuiLayout layout;
    private final Map<String, SlotRange> namedRanges;
    private final Map<String, Object> metadata;

    public GuiPage(long ignored, GuiInventory inventory, Player viewer) {
        this.layout = new GuiLayoutImpl(0L);
        this.namedRanges = new HashMap<>();
        this.metadata = new HashMap<>();
        this.guiInventory = inventory;
        this.viewer = viewer;
        this.grid = new GuiItem[inventory.e9(new Object[0])][inventory.e1(new Object[0])];
    }

    @Override
    public GuiInventory getGuiInventory() {
        return guiInventory;
    }

    @Override
    public GuiLayout getLayout() {
        return layout;
    }

    @Override
    public GuiItem[][] getItemsGrid() {
        return grid;
    }

    @Override
    public Optional<SlotRange> findRange(String id) {
        return Optional.ofNullable(namedRanges.get(id));
    }

    @Override
    public SlotRange createRange(String id, SlotDirection direction, int row, int col) {
        SlotRangeImpl range = new SlotRangeImpl(this, guiInventory, direction, 0L, row, col);
        namedRanges.put(id, range);
        return range;
    }

    @Override
    public SlotRange createRange(String id, SlotDirection direction, GridPosition position) {
        return createRange(id, direction, position.RJ(new Object[0]), position.RG(new Object[0]));
    }

    @Override
    public SlotRange createTemporaryRange(SlotDirection direction, int row, int col) {
        return new SlotRangeImpl(this, guiInventory, direction, 0L, row, col);
    }

    @Override
    public SlotRange createTemporaryRange(SlotDirection direction, GridPosition position) {
        return createTemporaryRange(direction, position.RJ(new Object[0]), position.RG(new Object[0]));
    }

    @Override
    public Optional<GuiItem> getItemAt(int row, int col) {
        if (row < 0 || row >= grid.length) {
            return Optional.empty();
        }
        if (col < 0 || col >= grid[row].length) {
            return Optional.empty();
        }
        return Optional.ofNullable(grid[row][col]);
    }

    @Override
    public Optional<GuiItem> getItemAt(GridPosition position) {
        return getItemAt(position.RJ(new Object[0]), position.RG(new Object[0]));
    }

    @Override
    public Optional<GuiItem> getItemBySlot(int slot) {
        int columns = guiInventory.e1(new Object[0]);
        int row = slot / columns;
        int col = slot % columns;
        return getItemAt(row, col);
    }

    @Override
    public PaginatedGui setItem(int row, int col, GuiItem item) {
        if (row < 0 || row >= grid.length) {
            return this;
        }
        if (col < 0 || col >= grid[row].length) {
            return this;
        }

        grid[row][col] = item;
        ItemStack stack = item == null ? null : item.qk(new Object[0]);
        updateOpenInventorySlot(row, col, stack);
        return this;
    }

    @Override
    public PaginatedGui setItem(GridPosition position, GuiItem item) {
        return setItem(position.RJ(new Object[0]), position.RG(new Object[0]), item);
    }

    @Override
    public PaginatedGui fillFirstEmpty(GuiItem item) {
        for (int row = 0; row < grid.length; row++) {
            for (int col = 0; col < grid[row].length; col++) {
                if (grid[row][col] == null) {
                    return setItem(row, col, item);
                }
            }
        }
        return this;
    }

    @Override
    public PaginatedGui applyLayout() {
        GuiItem[] pageItems = layout.a(new Object[0]);
        int index = 0;

        for (int row = 0; row < grid.length; row++) {
            for (int col = 0; col < grid[row].length; col++) {
                GuiItem item = index < pageItems.length ? pageItems[index] : null;
                setItem(row, col, item);
                index++;
            }
        }

        return this;
    }

    @Override
    public PaginatedGui fillRow(int row, GuiItem item) {
        if (row < 0 || row >= grid.length) {
            return this;
        }

        for (int col = 0; col < grid[row].length; col++) {
            setItem(row, col, item);
        }

        return this;
    }

    @Override
    public PaginatedGui fillColumn(int col, GuiItem item) {
        for (int row = 0; row < grid.length; row++) {
            setItem(row, col, item);
        }

        return this;
    }

    @Override
    public PaginatedGui fillAll(GuiItem item) {
        return fillRectangle(0, 0, guiInventory.e9(new Object[0]) - 1, guiInventory.e1(new Object[0]) - 1, item);
    }

    @Override
    public PaginatedGui fillRectangle(int startRow, int startCol, int endRow, int endCol, GuiItem item) {
        int minRow = Math.min(startRow, endRow);
        int maxRow = Math.max(startRow, endRow);
        int minCol = Math.min(startCol, endCol);
        int maxCol = Math.max(startCol, endCol);

        for (int row = minRow; row <= maxRow; row++) {
            for (int col = minCol; col <= maxCol; col++) {
                setItem(row, col, item);
            }
        }

        return this;
    }

    @Override
    public PaginatedGui fillRectangle(GridPosition from, GridPosition to, GuiItem item) {
        return fillRectangle(
            from.RJ(new Object[0]),
            from.RG(new Object[0]),
            to.RJ(new Object[0]),
            to.RG(new Object[0]),
            item
        );
    }

    @Override
    public Object getMetadata(String key) {
        return metadata.get(key);
    }

    @Override
    public Object getMetadataOrDefault(String key, Object fallback) {
        if (metadata.containsKey(key)) {
            return metadata.get(key);
        }
        return fallback;
    }

    @Override
    public PaginatedGui setMetadata(String key, Object value) {
        metadata.put(key, value);
        return this;
    }

    private void updateOpenInventorySlot(int row, int col, ItemStack item) {
        if (!guiInventory.eH(new Object[0]).N4(new Object[]{guiInventory}).contains(viewer)) {
            return;
        }

        Inventory topInventory = viewer.getOpenInventory().getTopInventory();
        int slot = guiInventory.e1(new Object[0]) * row + col;
        if (slot < 0 || slot >= topInventory.getSize()) {
            return;
        }

        topInventory.setItem(slot, item);
    }
}
