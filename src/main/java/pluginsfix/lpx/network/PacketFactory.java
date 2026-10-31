package pluginsfix.lpx.network;
import io.netty.buffer.ByteBuf;
import pluginsfix.lpx.api.player.ProtocolPlayer;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.netty.buffer.ByteBufHelper;
import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import org.jetbrains.annotations.Nullable;
public final class PacketFactory {
    private static int legacyState;
    private PacketFactory() {
    }
    public static PacketWrapper Sm(Object[] args) {
        ProtocolPlayer player = (ProtocolPlayer) args[0];
        ByteBuf buffer = (ByteBuf) args[2];
        boolean useClientVersion = (Boolean) args[3];
        PacketWrapper wrapper = createWrapper(player, useClientVersion);
        setBuffer(wrapper, buffer);
        return wrapper;
    }
    public static void Ms(@Nullable long ignored, @Nullable PacketWrapper wrapper) {
        ByteBuf buffer = getBuffer(wrapper);
        if (buffer == null) {
            return;
        }
        if (ByteBufHelper.refCnt(buffer) > 0) {
            ByteBufHelper.release(buffer);
        }
    }
    private static PacketWrapper createWrapper(ProtocolPlayer player, boolean useClientVersion) {
        ClientVersion clientVersion = player.getUser().getClientVersion();
        ServerVersion serverVersion = useClientVersion
            ? clientVersion.toServerVersion()
            : PacketEvents.getAPI().getServerManager().getVersion();
        for (Constructor<?> constructor : PacketWrapper.class.getDeclaredConstructors()) {
            Class<?>[] params = constructor.getParameterTypes();
            if (params.length != 3) {
                continue;
            }
            try {
                constructor.setAccessible(true);
                Object instance = constructor.newInstance(clientVersion, serverVersion, 0);
                return (PacketWrapper) instance;
            } catch (ReflectiveOperationException ignored) {
            }
        }
        throw new IllegalStateException("Unable to construct PacketWrapper for " + clientVersion + " -> " + serverVersion);
    }
    private static void setBuffer(PacketWrapper wrapper, ByteBuf buffer) {
        try {
            Field bufferField = PacketWrapper.class.getDeclaredField("buffer");
            bufferField.setAccessible(true);
            bufferField.set(wrapper, buffer);
        } catch (ReflectiveOperationException ex) {
            throw new IllegalStateException("Unable to set PacketWrapper buffer", ex);
        }
    }
    private static ByteBuf getBuffer(@Nullable PacketWrapper wrapper) {
        if (wrapper == null) {
            return null;
        }
        try {
            Field bufferField = PacketWrapper.class.getDeclaredField("buffer");
            bufferField.setAccessible(true);
            return (ByteBuf) bufferField.get(wrapper);
        } catch (ReflectiveOperationException ex) {
            return null;
        }
    }
    public static void SR(int value) {
        legacyState = value;
    }
    public static int Jl() {
        return legacyState;
    }
    public static int JE() {
        return legacyState == 0 ? 25 : 0;
    }
    private static RuntimeException a(RuntimeException runtimeException) {
        return runtimeException;
    }
}
