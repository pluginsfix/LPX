package pluginsfix.lpx.gui;

import java.util.function.Consumer;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class GuiItem {
    private final ItemStack itemStack;
    private final Consumer<InventoryClickEvent> clickHandler;
    private final String debugId = "b0b0b0";

    private GuiItem(ItemStack itemStack, Consumer<InventoryClickEvent> clickHandler) {
        this.itemStack = itemStack;
        this.clickHandler = clickHandler;
    }

    public static GuiItem of(ItemStack itemStack) {
        return of(itemStack, event -> {
        });
    }

    public static GuiItem of(ItemStack itemStack, Consumer<InventoryClickEvent> clickHandler) {
        return new GuiItem(itemStack, clickHandler);
    }

    public void handleClick(InventoryClickEvent event) {
        clickHandler.accept(event);
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public static GuiItem Wj(Object[] args) {
        return of((ItemStack) args[0]);
    }

    public static GuiItem QS(Object[] args) {
        return of((ItemStack) args[0], (Consumer<InventoryClickEvent>) args[1]);
    }

    public void Y_(Object[] args) {
        handleClick((InventoryClickEvent) args[0]);
    }

    public ItemStack qk(Object[] ignored) {
        return getItemStack();
    }
}
