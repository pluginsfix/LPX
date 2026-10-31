package pluginsfix.lpx.gui;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;

public class GuiInventory {
    private static final String UNKNOWN_PROVIDER_MESSAGE = "No inventory provider for type: ";

    private final GuiManagerImpl manager;
    private String id;
    private String title;
    private InventoryType inventoryType;
    private int rows;
    private int columns;
    private boolean reopenOnClose;
    private GuiCallback callback;
    private GuiInventory parent;
    private List<TypedConsumer<? extends Event>> eventConsumers;
    private static boolean debugFlag;

    GuiInventory(GuiManagerImpl manager) {
        this.manager = manager;
        this.eventConsumers = new ArrayList<>();
    }

    public static GuiInventoryBuilder builder() {
        return new GuiInventoryBuilder(0L);
    }

    public Inventory open(Player player) {
        return openPage(player, 0);
    }

    public Inventory openPage(Player player, int pageIndex) {
        manager.bY(new Object[]{player}).ifPresent(existing -> {
            for (TypedConsumer<? extends Event> consumer : existing.getEventConsumers()) {
                if (consumer.Fn(new Object[0]) == InventoryCloseEvent.class) {
                    consumer.pr(new Object[]{new InventoryCloseEvent(player.getOpenInventory())});
                }
            }
            manager.xS(new Object[]{player, null, 0L});
            manager.Lh(new Object[]{player, null, 0L});
        });

        GuiPage page = new GuiPage(0L, this, player);
        page.getLayout().setCurrentPage(pageIndex);
        manager.Lh(new Object[]{player, page, 0L});

        if (callback != null) {
            callback.c(new Object[]{player, page, 0L});
        }

        InventoryProvider provider = manager
            .sb(new Object[]{inventoryType, 0L})
            .orElseThrow(() -> new IllegalStateException(UNKNOWN_PROVIDER_MESSAGE + inventoryType.name()));

        Inventory inventory = provider.r(new Object[]{0L, this, player});
        manager.xS(new Object[]{player, this, 0L});
        return inventory;
    }

    public void close(Player player) {
        for (TypedConsumer<? extends Event> consumer : eventConsumers) {
            if (consumer.Fn(new Object[0]) == InventoryCloseEvent.class) {
                consumer.pr(new Object[]{new InventoryCloseEvent(player.getOpenInventory())});
            }
        }

        manager.xS(new Object[]{player, null, 0L});
        player.closeInventory();
        manager.Lh(new Object[]{player, null, 0L});
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public InventoryType getInventoryType() {
        return inventoryType;
    }

    public int getRows() {
        return rows;
    }

    public int getColumns() {
        return columns;
    }

    public boolean isReopenOnClose() {
        return reopenOnClose;
    }

    public void setReopenOnClose(boolean reopenOnClose) {
        this.reopenOnClose = reopenOnClose;
    }

    public GuiCallback getCallback() {
        return callback;
    }

    public Optional<GuiInventory> getParent() {
        return Optional.ofNullable(parent);
    }

    public GuiManagerImpl getManager() {
        return manager;
    }

    public List<TypedConsumer<? extends Event>> getEventConsumers() {
        return eventConsumers;
    }

    void setId(String id) {
        this.id = id;
    }

    void setTitle(String title) {
        this.title = title;
    }

    void setInventoryType(InventoryType inventoryType) {
        this.inventoryType = inventoryType;
    }

    void setRows(int rows) {
        this.rows = rows;
    }

    void setColumns(int columns) {
        this.columns = columns;
    }

    void setCallback(GuiCallback callback) {
        this.callback = callback;
    }

    void setParent(GuiInventory parent) {
        this.parent = parent;
    }

    void setEventConsumers(List<TypedConsumer<? extends Event>> eventConsumers) {
        this.eventConsumers = new ArrayList<>(eventConsumers);
    }

    public static void setDebugFlag(boolean value) {
        debugFlag = value;
    }

    public static boolean isDebugFlagEnabled() {
        return debugFlag;
    }

    public static boolean isDebugFlagDisabled() {
        return !debugFlag;
    }

    public static GuiInventoryBuilder ea(Object[] ignored) {
        return builder();
    }

    public Inventory lq(Object[] args) {
        return open((Player) args[1]);
    }

    public Inventory _D(Object[] args) {
        return openPage((Player) args[0], (Integer) args[2]);
    }

    public void lD(Object[] args) {
        close((Player) args[0]);
    }

    public String eR(Object[] ignored) {
        return getId();
    }

    public String ep(Object[] ignored) {
        return getTitle();
    }

    public InventoryType eD(Object[] ignored) {
        return getInventoryType();
    }

    public int e9(Object[] ignored) {
        return getRows();
    }

    public int e1(Object[] ignored) {
        return getColumns();
    }

    public boolean e7(Object[] ignored) {
        return isReopenOnClose();
    }

    public void ai(Object[] args) {
        setReopenOnClose((Boolean) args[0]);
    }

    public GuiCallback eL(Object[] ignored) {
        return getCallback();
    }

    public Optional<GuiInventory> ed(Object[] ignored) {
        return getParent();
    }

    public GuiManagerImpl eH(Object[] ignored) {
        return getManager();
    }

    List<TypedConsumer<? extends Event>> eE(Object[] ignored) {
        return getEventConsumers();
    }

    public static void aG(boolean value) {
        setDebugFlag(value);
    }

    public static boolean eo() {
        return isDebugFlagEnabled();
    }

    public static boolean eA() {
        return isDebugFlagDisabled();
    }
}
