package pluginsfix.lpx.gui;

import org.bukkit.entity.Player;

@FunctionalInterface
public interface GuiCallback {
    void initialize(Player player, PaginatedGui page);

    default void c(Object[] args) {
        initialize((Player) args[0], (PaginatedGui) args[1]);
    }
}
