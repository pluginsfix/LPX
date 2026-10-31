package pluginsfix.lpx.gui;

import pluginsfix.lpx.core.LPXCore;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

public class CheckSettingsGui implements GuiCallback {
    private final Format dateFormat;
    private final LPXCore core;

    public CheckSettingsGui(long ignored, LPXCore core) {
        this.dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        this.core = core;
    }

    @Override
    public void initialize(Player player, PaginatedGui page) {
        GuiItem filler = GuiItem.of(
            ItemBuilder.tC(new Object[]{Material.GRAY_STAINED_GLASS_PANE})
                .Ef(new Object[]{0L, ChatColor.DARK_GRAY + " "})
                .dk(new Object[0])
        );

        page.fillAll(filler);

        page.setItem(
            0,
            0,
            GuiItem.of(
                ItemBuilder.tC(new Object[]{Material.CLOCK})
                    .Ef(new Object[]{0L, ChatColor.GRAY + "Generated: " + formatTimestamp(System.currentTimeMillis())})
                    .dk(new Object[0])
            )
        );

        page.setItem(
            4,
            0,
            GuiItem.of(
                ItemBuilder.tC(new Object[]{Material.BARRIER})
                    .Ef(new Object[]{0L, ChatColor.RED + "Back"})
                    .dk(new Object[0]),
                (InventoryClickEvent clickEvent) -> core.sx(new Object[0]).getMainGuiInventory().open(player)
            )
        );

        page.setItem(
            4,
            3,
            GuiItem.of(
                ItemBuilder.tC(new Object[]{Material.ARROW})
                    .Ef(new Object[]{0L, ChatColor.YELLOW + "Previous Page"})
                    .dk(new Object[0]),
                (InventoryClickEvent clickEvent) -> {
                    page.getLayout().moveToPreviousPage();
                    createGuiInventory().openPage(player, page.getLayout().getCurrentPage());
                }
            )
        );

        page.setItem(
            4,
            5,
            GuiItem.of(
                ItemBuilder.tC(new Object[]{Material.ARROW})
                    .Ef(new Object[]{0L, ChatColor.YELLOW + "Next Page"})
                    .dk(new Object[0]),
                (InventoryClickEvent clickEvent) -> {
                    page.getLayout().moveToNextPage();
                    createGuiInventory().openPage(player, page.getLayout().getCurrentPage());
                }
            )
        );
    }

    public GuiInventory createGuiInventory() {
        return GuiInventory.ea(new Object[]{0L})
            .n7(new Object[]{"Check Settings"})
            .Gi(new Object[]{this})
            .Hd(new Object[]{core.sJ(new Object[0])})
            .NS(new Object[]{5, 9})
            .nu(new Object[]{ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + "LPX " + ChatColor.LIGHT_PURPLE + "Check Settings"})
            .ft(new Object[]{0L});
    }

    public GuiInventory f5(Object[] ignored) {
        return createGuiInventory();
    }

    private String formatTimestamp(long millis) {
        return dateFormat.format(new Date(millis));
    }

    private String VS(Object[] args) {
        return formatTimestamp((Long) args[0]);
    }
}
