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
                .Ef(new Object[]{0L, " "})
                .dk(new Object[0])
        );

        page.fillAll(filler);

        page.setItem(
            0,
            0,
            GuiItem.of(
                ItemBuilder.tC(new Object[]{Material.CLOCK})
                    .Ef(new Object[]{0L, "&#FFFF00◆ &fИнформация о модулях"})
                    .UN(new Object[]{0L, new String[]{
                        "&fВсего модулей: &#FFFF0014",
                        "&fСформировано: &#FFFF00" + formatTimestamp(System.currentTimeMillis())
                    }})
                    .dk(new Object[0])
            )
        );

        page.setItem(
            0,
            4,
            GuiItem.of(
                ItemBuilder.tC(new Object[]{Material.BEACON})
                    .Ef(new Object[]{0L, "&#FFFF00◆ &fСистема защиты LPX"})
                    .UN(new Object[]{0L, new String[]{
                        "&fВсе модули активны и защищают сервер",
                        "&fот эксплойтов и пакетных атак"
                    }})
                    .dk(new Object[0])
            )
        );

        page.setItem(
            1,
            1,
            GuiItem.of(
                ItemBuilder.tC(new Object[]{Material.BOOK})
                    .Ef(new Object[]{0L, "&#FFFF00◆ &fКниги &8(Book)"})
                    .UN(new Object[]{0L, new String[]{
                        "&fЗащита от краш-книг и переполнения NBT",
                        "&fСтатус: &#FFFF00Активен"
                    }})
                    .dk(new Object[0])
            )
        );

        page.setItem(
            1,
            2,
            GuiItem.of(
                ItemBuilder.tC(new Object[]{Material.COMMAND_BLOCK})
                    .Ef(new Object[]{0L, "&#FFFF00◆ &fКоманды &8(Command)"})
                    .UN(new Object[]{0L, new String[]{
                        "&fЗащита от флуда командами и инъекций",
                        "&fСтатус: &#FFFF00Активен"
                    }})
                    .dk(new Object[0])
            )
        );

        page.setItem(
            1,
            3,
            GuiItem.of(
                ItemBuilder.tC(new Object[]{Material.GRASS_BLOCK})
                    .Ef(new Object[]{0L, "&#FFFF00◆ &fКреатив &8(Creative)"})
                    .UN(new Object[]{0L, new String[]{
                        "&fКонтроль запрещённых предметов и NBT",
                        "&fСтатус: &#FFFF00Активен"
                    }})
                    .dk(new Object[0])
            )
        );

        page.setItem(
            1,
            4,
            GuiItem.of(
                ItemBuilder.tC(new Object[]{Material.REDSTONE})
                    .Ef(new Object[]{0L, "&#FFFF00◆ &fПакетный флуд &8(Flood)"})
                    .UN(new Object[]{0L, new String[]{
                        "&fЗащита от перегрузки пакетами и DOS",
                        "&fСтатус: &#FFFF00Активен"
                    }})
                    .dk(new Object[0])
            )
        );

        page.setItem(
            1,
            5,
            GuiItem.of(
                ItemBuilder.tC(new Object[]{Material.DIAMOND_SWORD})
                    .Ef(new Object[]{0L, "&#FFFF00◆ &fПредметы &8(Item)"})
                    .UN(new Object[]{0L, new String[]{
                        "&fКонтроль NBT-размеров и компонентов",
                        "&fСтатус: &#FFFF00Активен"
                    }})
                    .dk(new Object[0])
            )
        );

        page.setItem(
            1,
            6,
            GuiItem.of(
                ItemBuilder.tC(new Object[]{Material.NAME_TAG})
                    .Ef(new Object[]{0L, "&#FFFF00◆ &fNBT-теги &8(NBT)"})
                    .UN(new Object[]{0L, new String[]{
                        "&fГлубина вложенности и лимиты NBT",
                        "&fСтатус: &#FFFF00Активен"
                    }})
                    .dk(new Object[0])
            )
        );

        page.setItem(
            1,
            7,
            GuiItem.of(
                ItemBuilder.tC(new Object[]{Material.REPEATER})
                    .Ef(new Object[]{0L, "&#FFFF00◆ &fNetty &8(Netty)"})
                    .UN(new Object[]{0L, new String[]{
                        "&fКонтроль низкоуровневых Netty-буферов",
                        "&fСтатус: &#FFFF00Активен"
                    }})
                    .dk(new Object[0])
            )
        );

        page.setItem(
            2,
            1,
            GuiItem.of(
                ItemBuilder.tC(new Object[]{Material.MAP})
                    .Ef(new Object[]{0L, "&#FFFF00◆ &fПолезная нагрузка &8(Payload)"})
                    .UN(new Object[]{0L, new String[]{
                        "&fФильтрация кастомных каналов и данных",
                        "&fСтатус: &#FFFF00Активен"
                    }})
                    .dk(new Object[0])
            )
        );

        page.setItem(
            2,
            2,
            GuiItem.of(
                ItemBuilder.tC(new Object[]{Material.STONE})
                    .Ef(new Object[]{0L, "&#FFFF00◆ &fВзаимодействие &8(Place)"})
                    .UN(new Object[]{0L, new String[]{
                        "&fДистанция взаимодействия и установка блоков",
                        "&fСтатус: &#FFFF00Активен"
                    }})
                    .dk(new Object[0])
            )
        );

        page.setItem(
            2,
            3,
            GuiItem.of(
                ItemBuilder.tC(new Object[]{Material.COMPASS})
                    .Ef(new Object[]{0L, "&#FFFF00◆ &fДвижение &8(Position)"})
                    .UN(new Object[]{0L, new String[]{
                        "&fКоординаты, вращение и защита от пустоты",
                        "&fСтатус: &#FFFF00Активен"
                    }})
                    .dk(new Object[0])
            )
        );

        page.setItem(
            2,
            4,
            GuiItem.of(
                ItemBuilder.tC(new Object[]{Material.CRAFTING_TABLE})
                    .Ef(new Object[]{0L, "&#FFFF00◆ &fРецепты &8(Recipe)"})
                    .UN(new Object[]{0L, new String[]{
                        "&fВалидация крафтов и запросов рецептов",
                        "&fСтатус: &#FFFF00Активен"
                    }})
                    .dk(new Object[0])
            )
        );

        page.setItem(
            2,
            5,
            GuiItem.of(
                ItemBuilder.tC(new Object[]{Material.OAK_SIGN})
                    .Ef(new Object[]{0L, "&#FFFF00◆ &fТаблички &8(Sign)"})
                    .UN(new Object[]{0L, new String[]{
                        "&fЛимиты длины текста на табличках",
                        "&fСтатус: &#FFFF00Активен"
                    }})
                    .dk(new Object[0])
            )
        );

        page.setItem(
            2,
            6,
            GuiItem.of(
                ItemBuilder.tC(new Object[]{Material.PAPER})
                    .Ef(new Object[]{0L, "&#FFFF00◆ &fАвтодополнение &8(Tab)"})
                    .UN(new Object[]{0L, new String[]{
                        "&fЗащита от флуда Tab-комплитом",
                        "&fСтатус: &#FFFF00Активен"
                    }})
                    .dk(new Object[0])
            )
        );

        page.setItem(
            2,
            7,
            GuiItem.of(
                ItemBuilder.tC(new Object[]{Material.CHEST})
                    .Ef(new Object[]{0L, "&#FFFF00◆ &fИнтерфейсы &8(Window)"})
                    .UN(new Object[]{0L, new String[]{
                        "&fВалидация слотов и действий в инвентаре",
                        "&fСтатус: &#FFFF00Активен"
                    }})
                    .dk(new Object[0])
            )
        );

        page.setItem(
            4,
            0,
            GuiItem.of(
                ItemBuilder.tC(new Object[]{Material.BARRIER})
                    .Ef(new Object[]{0L, "&#FB8808▶ &fНазад"})
                    .UN(new Object[]{0L, new String[]{
                        "&fВернуться в главное меню"
                    }})
                    .dk(new Object[0]),
                (InventoryClickEvent clickEvent) -> core.sx(new Object[0]).getMainGuiInventory().open(player)
            )
        );

        page.setItem(
            4,
            3,
            GuiItem.of(
                ItemBuilder.tC(new Object[]{Material.ARROW})
                    .Ef(new Object[]{0L, "&#FFFF00◆ &fПредыдущая страница"})
                    .UN(new Object[]{0L, new String[]{
                        "&fПерейти на страницу назад"
                    }})
                    .dk(new Object[0]),
                (InventoryClickEvent clickEvent) -> {
                    page.getLayout().moveToPreviousPage();
                    createGuiInventory().openPage(player, page.getLayout().getCurrentPage());
                }
            )
        );

        page.setItem(
            4,
            4,
            GuiItem.of(
                ItemBuilder.tC(new Object[]{Material.NETHER_STAR})
                    .Ef(new Object[]{0L, "&#FFFF00◆ &fВсе модули работают"})
                    .UN(new Object[]{0L, new String[]{
                        "&fЗащита активна в реальном времени"
                    }})
                    .dk(new Object[0])
            )
        );

        page.setItem(
            4,
            5,
            GuiItem.of(
                ItemBuilder.tC(new Object[]{Material.ARROW})
                    .Ef(new Object[]{0L, "&#FFFF00◆ &fСледующая страница"})
                    .UN(new Object[]{0L, new String[]{
                        "&fПерейти на следующую страницу"
                    }})
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
            .qo(new Object[]{false})
            .nu(new Object[]{net.md_5.bungee.api.ChatColor.of("#FFFF00") + "" + ChatColor.BOLD + "LPX " + ChatColor.DARK_GRAY + "| " + ChatColor.WHITE + "Настройки проверок"})
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
