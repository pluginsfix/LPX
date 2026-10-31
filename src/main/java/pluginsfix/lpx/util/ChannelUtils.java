package pluginsfix.lpx.util;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import com.github.retrooper.packetevents.netty.buffer.ByteBufHelper;
import com.github.retrooper.packetevents.protocol.item.ItemStack;
import com.github.retrooper.packetevents.protocol.nbt.NBT;
import com.github.retrooper.packetevents.protocol.nbt.NBTByte;
import com.github.retrooper.packetevents.protocol.nbt.NBTByteArray;
import com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import com.github.retrooper.packetevents.protocol.nbt.NBTDouble;
import com.github.retrooper.packetevents.protocol.nbt.NBTFloat;
import com.github.retrooper.packetevents.protocol.nbt.NBTInt;
import com.github.retrooper.packetevents.protocol.nbt.NBTIntArray;
import com.github.retrooper.packetevents.protocol.nbt.NBTList;
import com.github.retrooper.packetevents.protocol.nbt.NBTLong;
import com.github.retrooper.packetevents.protocol.nbt.NBTLongArray;
import com.github.retrooper.packetevents.protocol.nbt.NBTShort;
import com.github.retrooper.packetevents.protocol.nbt.NBTString;
import java.nio.charset.StandardCharsets;
import java.util.Map;
public final class ChannelUtils {
    private static final int MAX_NBT_RECURSION = 64;
    private ChannelUtils() {
    }
    public static int PB(Object[] args) {
        NBTCompound compound = (NBTCompound) args[1];
        int depth = (Integer) args[2];
        if (compound == null) {
            return 0;
        }
        int size = 0;
        for (Map.Entry<String, NBT> entry : compound.getTags().entrySet()) {
            size += entry.getKey().getBytes(StandardCharsets.UTF_8).length;
            size += Oy(new Object[]{0L, entry.getValue(), depth + 1});
        }
        return size;
    }
    @SuppressWarnings("unchecked")
    public static int Oy(Object[] args) {
        NBT tag = (NBT) args[1];
        int depth = (Integer) args[2];
        if (tag == null) {
            return 0;
        }
        if (depth > MAX_NBT_RECURSION) {
            throw new RuntimeException("NBT recursion depth exceeded: " + depth);
        }
        if (tag instanceof NBTByte) {
            return 1;
        }
        if (tag instanceof NBTShort) {
            return 2;
        }
        if (tag instanceof NBTInt || tag instanceof NBTFloat) {
            return 4;
        }
        if (tag instanceof NBTLong || tag instanceof NBTDouble) {
            return 8;
        }
        if (tag instanceof NBTString stringTag) {
            return stringTag.getValue().getBytes(StandardCharsets.UTF_8).length;
        }
        if (tag instanceof NBTByteArray byteArray) {
            return byteArray.getValue().length;
        }
        if (tag instanceof NBTIntArray intArray) {
            return intArray.getValue().length * 4;
        }
        if (tag instanceof NBTLongArray longArray) {
            return longArray.getValue().length * 8;
        }
        if (tag instanceof NBTCompound compound) {
            return PB(new Object[]{0L, compound, depth + 1});
        }
        if (tag instanceof NBTList<?> listTag) {
            int total = 0;
            for (NBT child : listTag.getTags()) {
                total += Oy(new Object[]{0L, child, depth + 1});
            }
            return total;
        }
        throw new RuntimeException("Unsupported NBT type: " + tag.getClass().getSimpleName());
    }
    public static int bp(Object[] args) {
        ItemStack itemStack = (ItemStack) args[1];
        if (itemStack == null) {
            return 0;
        }
        return PB(new Object[]{0L, itemStack.getNBT(), 0});
    }
    public static boolean nB(Object[] args) {
        NBTCompound compound = (NBTCompound) args[0];
        String query = (String) args[2];
        if (compound == null || query == null || query.isEmpty()) {
            return false;
        }
        for (NBT value : compound.getTags().values()) {
            if (aM(new Object[]{value, query, 0L})) {
                return true;
            }
        }
        return false;
    }
    public static boolean aM(Object[] args) {
        Object first = args[0];
        String query = (String) args[1];
        if (!(first instanceof NBT nbt) || query == null || query.isEmpty()) {
            return false;
        }
        if (nbt instanceof NBTCompound compound) {
            if (containsPath(compound, query)) {
                return true;
            }
            for (Map.Entry<String, NBT> entry : compound.getTags().entrySet()) {
                if (entry.getKey().equalsIgnoreCase(query)) {
                    return true;
                }
                if (aM(new Object[]{entry.getValue(), query, 0L})) {
                    return true;
                }
            }
            return false;
        }
        if (nbt instanceof NBTList<?> listTag) {
            for (NBT child : listTag.getTags()) {
                if (aM(new Object[]{child, query, 0L})) {
                    return true;
                }
            }
        }
        return false;
    }
    public static void CC(Object[] args) {
        ChannelHandlerContext context = (ChannelHandlerContext) args[0];
        context.channel().close();
        context.channel().flush();
    }
    public static void Mv(Object[] args) {
        Channel channel = (Channel) args[0];
        channel.close();
        channel.flush();
    }
    public static void nj(Object[] args) {
        ByteBuf buffer = (ByteBuf) args[0];
        buffer.readBytes(buffer.readableBytes());
    }
    public static void nX(Object[] args) {
        ByteBuf buffer = (ByteBuf) args[1];
        if (buffer == null) {
            return;
        }
        if (buffer.refCnt() > 0) {
            buffer.readBytes(buffer.readableBytes());
        }
        nO(new Object[]{buffer, 0L});
    }
    public static void nO(Object[] args) {
        ByteBuf buffer = (ByteBuf) args[0];
        if (buffer == null) {
            return;
        }
        int refCount = buffer.refCnt();
        if (refCount > 0) {
            buffer.release(refCount);
        }
    }
    public static long Xs(Object[] args) {
        return ByteBufHelper.capacity(args[0]);
    }
    private static boolean containsPath(NBTCompound compound, String query) {
        if (!query.contains(".")) {
            return compound.getTags().containsKey(query);
        }
        String[] parts = query.split("\\.");
        NBTCompound current = compound;
        for (int i = 0; i < parts.length; i++) {
            NBT child = current.getTagOrNull(parts[i]);
            if (child == null) {
                return false;
            }
            if (i == parts.length - 1) {
                return true;
            }
            if (!(child instanceof NBTCompound nextCompound)) {
                return false;
            }
            current = nextCompound;
        }
        return false;
    }
}
