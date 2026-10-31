package pluginsfix.lpx.gui;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.event.Event;
import org.bukkit.event.inventory.InventoryType;
public final class GuiInventoryBuilder {
    private static final String DEFAULT_ID = "default";
    private static final String MISSING_CALLBACK = "Gui callback is not set.";
    private static final String MISSING_MANAGER = "Gui manager is not set.";
    private final List<TypedConsumer<? extends Event>> listeners = new ArrayList<>();
    private String id = DEFAULT_ID;
    private String title = "";
    private InventoryType inventoryType = InventoryType.CHEST;
    private int rows = 6;
    private int columns = 9;
    private boolean reopenOnClose = true;
    private GuiManagerImpl manager;
    private GuiCallback callback;
    private GuiInventory parent;
    GuiInventoryBuilder(long ignored) {
    }
    public GuiInventoryBuilder n7(Object[] args) {
        this.id = (String) args[0];
        return this;
    }
    public GuiInventoryBuilder nu(Object[] args) {
        this.title = (String) args[0];
        return this;
    }
    public GuiInventoryBuilder zK(Object[] args) {
        this.inventoryType = (InventoryType) args[0];
        return this;
    }
    public GuiInventoryBuilder NS(Object[] args) {
        this.rows = (Integer) args[0];
        this.columns = (Integer) args[1];
        return this;
    }
    public GuiInventoryBuilder qo(Object[] args) {
        this.reopenOnClose = (Boolean) args[0];
        return this;
    }
    public GuiInventoryBuilder Gi(Object[] args) {
        this.callback = (GuiCallback) args[0];
        return this;
    }
    public GuiInventoryBuilder Ed(Object[] args) {
        this.parent = (GuiInventory) args[0];
        return this;
    }
    public GuiInventoryBuilder nl(Object[] args) {
        this.listeners.add((TypedConsumer<? extends Event>) args[0]);
        return this;
    }
    public GuiInventoryBuilder Hd(Object[] args) {
        this.manager = (GuiManagerImpl) args[0];
        return this;
    }
    public GuiInventory ft(Object[] ignored) {
        if (callback == null) {
            throw new IllegalStateException(MISSING_CALLBACK);
        }
        if (manager == null) {
            throw new IllegalStateException(MISSING_MANAGER);
        }
        GuiInventory guiInventory = new GuiInventory(manager);
        guiInventory.setId(id);
        guiInventory.setTitle(title);
        guiInventory.setInventoryType(inventoryType);
        guiInventory.setRows(rows);
        guiInventory.setColumns(columns);
        guiInventory.setReopenOnClose(reopenOnClose);
        guiInventory.setCallback(callback);
        guiInventory.setParent(parent);
        guiInventory.setEventConsumers(listeners);
        return guiInventory;
    }
}
