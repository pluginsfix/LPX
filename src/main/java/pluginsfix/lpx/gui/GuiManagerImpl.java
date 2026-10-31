package pluginsfix.lpx.gui;

import pluginsfix.lpx.core.LPXCore;
import pluginsfix.lpx.listeners.impl.InventoryEventListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

public class GuiManagerImpl {
    private final Plugin plugin;
    private final LPXCore core;
    private final PluginManager pluginManager;
    private final Map<Player, GuiInventory> openedInventories;
    private final Map<Player, PaginatedGui> openedPages;
    private final List<InventoryProvider> defaultProviders;
    private final List<InventoryProvider> customProviders;

    public GuiManagerImpl(LPXCore core) {
        this.plugin = core.getPlugin();
        this.core = core;
        this.pluginManager = Bukkit.getPluginManager();
        this.openedInventories = new HashMap<>();
        this.openedPages = new HashMap<>();
        this.defaultProviders = Arrays.asList(new DefaultInventoryProvider(), new GuiTypeRegistry());
        this.customProviders = new ArrayList<>();
    }

    public void registerListeners() {
        pluginManager.registerEvents((Listener) new InventoryEventListener(this), plugin);
    }

    public Optional<InventoryProvider> findProvider(InventoryType inventoryType) {
        Optional<InventoryProvider> custom = customProviders
            .stream()
            .filter(provider -> provider.supports(inventoryType))
            .findFirst();
        if (custom.isPresent()) {
            return custom;
        }
        return defaultProviders
            .stream()
            .filter(provider -> provider.supports(inventoryType))
            .findFirst();
    }

    public void registerProviders(InventoryProvider... providers) {
        customProviders.addAll(Arrays.asList(providers));
    }

    public List<Player> getViewersForInventory(GuiInventory target) {
        List<Player> viewers = new ArrayList<>();
        openedInventories.forEach((player, inventory) -> {
            if (target.equals(inventory)) {
                viewers.add(player);
            }
        });
        return viewers;
    }

    public Optional<GuiInventory> getOpenedInventory(Player player) {
        return Optional.ofNullable(openedInventories.get(player));
    }

    public void setOpenedInventory(Player player, GuiInventory inventory) {
        if (inventory == null) {
            openedInventories.remove(player);
            return;
        }
        openedInventories.put(player, inventory);
    }

    public Optional<PaginatedGui> getOpenedPage(Player player) {
        return Optional.ofNullable(openedPages.get(player));
    }

    public void setOpenedPage(Player player, PaginatedGui page) {
        if (page == null) {
            openedPages.remove(player);
            return;
        }
        openedPages.put(player, page);
    }

    public Plugin getPlugin() {
        return plugin;
    }

    public LPXCore getCore() {
        return core;
    }

    public void clearOpenedState(Player player) {
        openedInventories.remove(player);
        openedPages.remove(player);
    }

    public Map<Player, GuiInventory> getOpenedInventoriesSnapshot() {
        return new HashMap<>(openedInventories);
    }

    public void MN(Object[] ignored) {
        registerListeners();
    }

    public Optional<InventoryProvider> sb(Object[] args) {
        return findProvider((InventoryType) args[0]);
    }

    public void cC(Object... args) {
        registerProviders((InventoryProvider[]) args[0]);
    }

    public List<Player> N4(Object[] args) {
        return getViewersForInventory((GuiInventory) args[0]);
    }

    public Optional<GuiInventory> bY(Object[] args) {
        return getOpenedInventory((Player) args[0]);
    }

    protected void xS(Object[] args) {
        setOpenedInventory((Player) args[0], (GuiInventory) args[1]);
    }

    public Optional<PaginatedGui> bN(Object[] args) {
        return getOpenedPage((Player) args[0]);
    }

    protected void Lh(Object[] args) {
        setOpenedPage((Player) args[0], (PaginatedGui) args[1]);
    }
}
