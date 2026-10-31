package pluginsfix.lpx.gui;

import pluginsfix.lpx.core.LPXCore;
import com.github.retrooper.packetevents.PacketEvents;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

public class MainGui implements GuiCallback {
    private static final String GUI_PERMISSION = "lpx.gui";
    private static final String RELOAD_PERMISSION = "lpx.reload";
    private static final String RELOAD_COMMAND = "lpx reload";

    private final LPXCore core;

    public MainGui(LPXCore core) {
        this.core = core;
    }

    @Override
    public void initialize(Player player, PaginatedGui page) {
        String versionName = PacketEvents.getAPI().getServerManager().getVersion().getReleaseName();

        page.setItem(
            0,
            0,
            GuiItem.of(
                ItemBuilder.tC(new Object[]{Material.WATER_BUCKET})
                    .Ef(new Object[]{0L, ChatColor.GRAY + "Server version: " + versionName})
                    .dk(new Object[0])
            )
        );

        if (player.hasPermission(RELOAD_PERMISSION)) {
            page.setItem(
                0,
                4,
                GuiItem.of(
                    ItemBuilder.tC(new Object[]{Material.NETHER_STAR})
                        .Ef(new Object[]{0L, ChatColor.DARK_PURPLE + "Reload Config"})
                        .dk(new Object[0]),
                    (InventoryClickEvent clickEvent) -> {
                        player.performCommand(RELOAD_COMMAND);
                        player.closeInventory();
                    }
                )
            );
        } else {
            page.setItem(
                0,
                4,
                GuiItem.of(
                    ItemBuilder.tC(new Object[]{Material.BARRIER})
                        .Ef(new Object[]{0L, ChatColor.DARK_PURPLE + "Reload Config"})
                        .UN(new Object[]{0L, new String[]{ChatColor.RED + "" + ChatColor.BOLD + "No permission"}})
                        .dk(new Object[0])
                )
            );
        }

        page.setItem(
            0,
            8,
            GuiItem.of(
                ItemBuilder.tC(new Object[]{Material.BOOK})
                    .Ef(new Object[]{0L, ChatColor.DARK_PURPLE + "Open Check Settings"})
                    .dk(new Object[0]),
                (InventoryClickEvent clickEvent) -> openCheckSettings(player)
            )
        );
    }

    public GuiInventory createGuiInventory() {
        return GuiInventory.ea(new Object[]{0L})
            .n7(new Object[]{"main"})
            .Gi(new Object[]{this})
            .Hd(new Object[]{core.sJ(new Object[0])})
            .NS(new Object[]{1, 9})
            .nu(new Object[]{ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + "LPX " + ChatColor.LIGHT_PURPLE + "Main"})
            .ft(new Object[]{0L});
    }

    public GuiInventory s_(Object[] ignored) {
        return createGuiInventory();
    }

    private void openCheckSettings(Player player) {
        if (!player.hasPermission(GUI_PERMISSION)) {
            return;
        }
        core.sx(new Object[0]).createCheckSettingsGui().open(player);
    }
}
