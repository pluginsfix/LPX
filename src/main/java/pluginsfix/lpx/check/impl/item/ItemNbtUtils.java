package pluginsfix.lpx.check.impl.item;
import pluginsfix.lpx.api.player.ProtocolPlayer;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.protocol.item.HashedStack;
import com.github.retrooper.packetevents.protocol.item.ItemStack;
import com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import com.github.retrooper.packetevents.protocol.nbt.NBTList;
import com.github.retrooper.packetevents.protocol.nbt.NBTString;
import java.util.Optional;
public final class ItemNbtUtils {
    private static final String DISPLAY_TAG = "display";
    private static final String DISPLAY_NAME_TAG = "Name";
    private static final String DISPLAY_LORE_TAG = "Lore";
    private ItemNbtUtils(long ignored) {
        throw new UnsupportedOperationException("Utility class");
    }
    public static Optional<String> bF(Object[] args) {
        ItemStack itemStack = (ItemStack) args[1];
        if (itemStack == null) {
            return Optional.empty();
        }
        NBTCompound root = itemStack.getNBT();
        if (root == null) {
            return Optional.empty();
        }
        NBTCompound display = root.getCompoundTagOrNull(DISPLAY_TAG);
        if (display != null) {
            String displayName = display.getStringTagValueOrNull(DISPLAY_NAME_TAG);
            if (displayName != null) {
                return Optional.of(displayName);
            }
        }
        return Optional.ofNullable(root.getStringTagValueOrNull(DISPLAY_NAME_TAG));
    }
    public static Optional<NBTList<NBTString>> bz(Object[] args) {
        ItemStack itemStack = (ItemStack) args[1];
        if (itemStack == null) {
            return Optional.empty();
        }
        NBTCompound root = itemStack.getNBT();
        if (root == null) {
            return Optional.empty();
        }
        NBTCompound display = root.getCompoundTagOrNull(DISPLAY_TAG);
        if (display == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(display.getStringListTagOrNull(DISPLAY_LORE_TAG));
    }
    public static boolean bt(Object[] args) {
        ItemStack itemStack = (ItemStack) args[0];
        if (itemStack == null) {
            return false;
        }
        boolean hasComponentPatches = false;
        try {
            hasComponentPatches = !itemStack.getComponents().getPatches().isEmpty();
        } catch (Throwable ignored) {
            hasComponentPatches = false;
        }
        NBTCompound nbt = itemStack.getNBT();
        boolean hasNbt = nbt != null && !nbt.isEmpty();
        return hasComponentPatches || hasNbt;
    }
    public static boolean bf(Object[] args) {
        HashedStack hashedStack = (HashedStack) args[0];
        if (hashedStack == null) {
            return true;
        }
        return hashedStack.getComponents().getAddedComponents().isEmpty();
    }
    public static boolean uo(Object[] args) {
        ProtocolPlayer player = (ProtocolPlayer) args[1];
        return supportsHashedStacks(player);
    }
    public static boolean uB(Object[] args) {
        ProtocolPlayer player = (ProtocolPlayer) args[0];
        return supportsHashedStacks(player);
    }
    private static boolean supportsHashedStacks(ProtocolPlayer player) {
        if (player == null) {
            return false;
        }
        if (player.usesHashedStacks()) {
            return true;
        }
        return PacketEvents.getAPI()
            .getServerManager()
            .getVersion()
            .isNewerThanOrEquals(ServerVersion.V_1_21_5);
    }
    private static UnsupportedOperationException a(UnsupportedOperationException exception) {
        return exception;
    }
}
