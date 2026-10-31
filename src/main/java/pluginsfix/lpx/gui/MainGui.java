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

        GuiItem filler = GuiItem.of(
            ItemBuilder.tC(new Object[]{Material.GRAY_STAINED_GLASS_PANE})
                .Ef(new Object[]{0L, " "})
                .dk(new Object[0])
        );
        page.fillAll(filler);

        page.setItem(
            0,
            0,
            GuiItem.of(
                ItemBuilder.tC(new Object[]{Material.BEACON})
                    .Ef(new Object[]{0L, "&#FFFF00◆ &fИнформация о сервере"})
                    .UN(new Object[]{0L, new String[]{
                        "&fВерсия сервера: &#FFFF00" + versionName,
                        "&fСтатус защиты: &#FFFF00Активна"
                    }})
                    .dk(new Object[0])
            )
        );

        page.setItem(
            0,
            2,
            GuiItem.of(
                ItemBuilder.tC(new Object[]{Material.COMPARATOR})
                    .Ef(new Object[]{0L, "&#FFFF00◆ &fСетевой мониторинг"})
                    .UN(new Object[]{0L, new String[]{
                        "&fОтслеживание Netty-потоков и пакетов",
                        "&fНажмите для переключения Watchdog"
                    }})
                    .dk(new Object[0]),
                (InventoryClickEvent clickEvent) -> {
                    player.performCommand("lpx watchdog");
                    player.closeInventory();
                }
            )
        );

        if (player.hasPermission(RELOAD_PERMISSION)) {
            page.setItem(
                0,
                4,
                GuiItem.of(
                    ItemBuilder.tC(new Object[]{Material.NETHER_STAR})
                        .Ef(new Object[]{0L, "&#FFFF00◆ &fПерезагрузить конфигурацию"})
                        .UN(new Object[]{0L, new String[]{
                            "&fНажмите для перезагрузки параметров",
                            "&fиз файлов &#FFFF00config.yml &fи &#FFFF00messages.yml"
                        }})
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
                        .Ef(new Object[]{0L, "&#FB8808▶ &fПерезагрузка недоступна"})
                        .UN(new Object[]{0L, new String[]{
                            "&#FB8808Недостаточно прав для выполнения"
                        }})
                        .dk(new Object[0])
                )
            );
        }

        page.setItem(
            0,
            6,
            GuiItem.of(
                ItemBuilder.tC(new Object[]{Material.BELL})
                    .Ef(new Object[]{0L, "&#FFFF00◆ &fОповещения защиты"})
                    .UN(new Object[]{0L, new String[]{
                        "&fПереключение вывода срабатываний в чат",
                        "&fНажмите для переключения"
                    }})
                    .dk(new Object[0]),
                (InventoryClickEvent clickEvent) -> {
                    player.performCommand("lpx alerts");
                    player.closeInventory();
                }
            )
        );

        page.setItem(
            0,
            8,
            GuiItem.of(
                ItemBuilder.tC(new Object[]{Material.BOOK})
                    .Ef(new Object[]{0L, "&#FFFF00◆ &fНастройки проверок"})
                    .UN(new Object[]{0L, new String[]{
                        "&fПросмотр и тонкая настройка",
                        "&fвсех модулей детекции LPX"
                    }})
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
            .qo(new Object[]{false})
            .nu(new Object[]{net.md_5.bungee.api.ChatColor.of("#FFFF00") + "" + ChatColor.BOLD + "LPX " + ChatColor.DARK_GRAY + "| " + ChatColor.WHITE + "Главное меню"})
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
