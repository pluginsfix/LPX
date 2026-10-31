package pluginsfix.lpx.listeners.impl;
import pluginsfix.lpx.core.LPXCore;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
public class RedstoneListener implements Listener {
    private static final String REDSTONE = "REDSTONE";
    private static final String OBSERVER = "OBSERVER";
    private final LPXCore Z;
    private final BlockFace[] e = BlockFace.values();
    public RedstoneListener(LPXCore core) {
        this.Z = core;
    }
    @EventHandler
    public void onRedstoneEvent(BlockRedstoneEvent event) {
        if (!Z.sZ(new Object[0]).Bw(new Object[0]).Yn(new Object[0])) {
            return;
        }
        Block block = event.getBlock();
        if (!isWatchedBlock(block)) {
            return;
        }
        if (lQ(new Object[]{0L, block})) {
            event.setNewCurrent(event.getOldCurrent());
        }
    }
    private boolean lQ(Object[] args) {
        Block block = (Block) args[1];
        for (BlockFace face : e) {
            if (block.getRelative(face).getType().name().contains(OBSERVER)) {
                return true;
            }
        }
        return false;
    }
    private boolean isWatchedBlock(Block block) {
        String materialName = block.getType().name();
        return materialName.contains(REDSTONE) || materialName.contains(OBSERVER);
    }
    private static RuntimeException a(RuntimeException runtimeException) {
        return runtimeException;
    }
}
