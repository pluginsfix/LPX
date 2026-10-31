package pluginsfix.lpx.check.impl.nbt;
import pluginsfix.lpx.api.check.CheckResult;
import pluginsfix.lpx.api.check.FlagInfo;
import pluginsfix.lpx.api.check.options.CheckOptions;
import pluginsfix.lpx.api.player.ProtocolPlayer;
import pluginsfix.lpx.check.base.AbstractCheckT;
import com.github.retrooper.packetevents.protocol.item.ItemStack;
import com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import com.github.retrooper.packetevents.protocol.nbt.NBTNumber;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientCreativeInventoryAction;
import org.bukkit.entity.Player;
import org.bukkit.World;
import org.bukkit.WorldBorder;
public class NBTCheckWorldBorder extends AbstractCheckT<WrapperPlayClientCreativeInventoryAction> {
    private static int X = 9;
    public NBTCheckWorldBorder(ProtocolPlayer player, CheckOptions options) {
        super(player, options);
    }
    @Override
    protected CheckResult Bu(Object[] args) {
        long token = (Long) args[0];
        ProtocolPlayer player = (ProtocolPlayer) args[1];
        WrapperPlayClientCreativeInventoryAction packet = (WrapperPlayClientCreativeInventoryAction) args[2];
        return R7(new Object[]{token, player, packet});
    }
    public CheckResult R7(Object[] args) {
        long token = (Long) args[0];
        WrapperPlayClientCreativeInventoryAction packet = (WrapperPlayClientCreativeInventoryAction) args[2];
        Player player = F.getPlayer().orElse(null);
        if (player == null) {
            return CheckResult.ALLOW;
        }
        ItemStack itemStack = packet.getItemStack();
        if (itemStack == null) {
            return CheckResult.ALLOW;
        }
        NBTCompound root = itemStack.getNBT();
        if (root == null) {
            return CheckResult.ALLOW;
        }
        NBTCompound blockEntityTag = root.getCompoundTagOrNull("BlockEntityTag");
        if (blockEntityTag == null) {
            return CheckResult.ALLOW;
        }
        NBTNumber xTag = blockEntityTag.getNumberTagOrNull("x");
        NBTNumber zTag = blockEntityTag.getNumberTagOrNull("z");
        if (xTag == null || zTag == null) {
            return CheckResult.ALLOW;
        }
        int x = xTag.getAsInt();
        int z = zTag.getAsInt();
        int chunkX = x >> 4;
        int chunkZ = z >> 4;
        World world = player.getWorld();
        boolean chunkLoaded = world.isChunkLoaded(chunkX, chunkZ);
        boolean insideBorder = isInsideBorder(world.getWorldBorder(), x, z);
        if (chunkLoaded && insideBorder) {
            return CheckResult.ALLOW;
        }
        AZ(new Object[]{
            packet,
            token,
            new FlagInfo[]{
                FlagInfo.of("world", world.getName()),
                FlagInfo.of("x", x),
                FlagInfo.of("z", z),
                FlagInfo.of("chunk-x", chunkX),
                FlagInfo.of("chunk-z", chunkZ),
                FlagInfo.of("chunk-loaded", chunkLoaded),
                FlagInfo.of("inside-border", insideBorder)
            }
        });
        return CheckResult.DENY;
    }
    private boolean isInsideBorder(WorldBorder border, int x, int z) {
        if (border == null) {
            return true;
        }
        double halfSize = border.getSize() / 2.0D;
        double minX = border.getCenter().getX() - halfSize;
        double maxX = border.getCenter().getX() + halfSize;
        double minZ = border.getCenter().getZ() - halfSize;
        double maxZ = border.getCenter().getZ() + halfSize;
        return x >= minX && x <= maxX && z >= minZ && z <= maxZ;
    }
    public static void EI(int n) {
        X = n;
    }
    public static int jP() {
        return X;
    }
    public static int jM() {
        return X == 0 ? 9 : 0;
    }
    private static Exception a(Exception exception) {
        return exception;
    }
}
