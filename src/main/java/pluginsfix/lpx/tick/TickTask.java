package pluginsfix.lpx.tick;

import pluginsfix.lpx.api.check.abstraction.Check;
import pluginsfix.lpx.api.player.ProtocolPlayer;
import pluginsfix.lpx.api.protocol.CheckContainer;
import pluginsfix.lpx.core.LPXCore;
import org.bukkit.scheduler.BukkitRunnable;

public class TickTask extends BukkitRunnable {
    private final LPXCore core;
    private static int[] legacyState;

    public TickTask(long ignored, LPXCore core) {
        this.core = core;
    }

    @Override
    public void run() {
        for (ProtocolPlayer player : core.getRegistry().getPlayers().values()) {
            for (CheckContainer<?> container : player.getChecks().values()) {
                for (Check<?> check : container) {
                    check.getOptions().setVl(0);
                    check.getOptions().setBuffer(0.0);
                }
            }
        }
    }

    public static void setLegacyState(int[] value) {
        legacyState = value;
    }

    public static int[] getLegacyState() {
        return legacyState;
    }

    public static void qW(int[] value) {
        setLegacyState(value);
    }

    public static int[] Eq() {
        return getLegacyState();
    }
}
