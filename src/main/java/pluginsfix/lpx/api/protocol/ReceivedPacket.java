package pluginsfix.lpx.api.protocol;
import com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;
public class ReceivedPacket {
    private static final ThreadLocal<NumberFormat> FORMAT = ThreadLocal.withInitial(() -> new DecimalFormat("0.0000"));
    private final String playerName;
    private final long createdAtNanos;
    private final long receivedAtTick;
    private final PacketTypeCommon packetType;
    private final long size;
    private Object packet;
    private long elapsedTime;
    private String packetString;
    private static String legacyState;
    public ReceivedPacket(String playerName, long receivedAtTick, PacketTypeCommon packetType, long size, Object packet) {
        this.playerName = playerName;
        this.receivedAtTick = receivedAtTick;
        this.packetType = packetType;
        this.createdAtNanos = System.nanoTime();
        this.size = size;
        this.packet = packet;
    }
    public String getPlayerName() {
        return playerName;
    }
    public long getReceivedAtTick() {
        return receivedAtTick;
    }
    public PacketTypeCommon getPacketType() {
        return packetType;
    }
    public Object getPacket() {
        return packet;
    }
    public void setPacket(Object packet) {
        this.packet = packet;
    }
    public long getSize() {
        return size;
    }
    public long getElapsedTime() {
        if (elapsedTime == 0L) {
            elapsedTime = System.nanoTime() - createdAtNanos;
        }
        return elapsedTime;
    }
    public String getFormattedElapsedTime() {
        return FORMAT.get().format(getElapsedTime() / 1_000_000.0D);
    }
    @Override
    public String toString() {
        if (packetString == null) {
            packetString = buildPacketString().replace('\n', ' ').trim();
        }
        return packetString;
    }
    private String buildPacketString() {
        if (packet == null) {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        for (Field field : packet.getClass().getDeclaredFields()) {
            if (!field.getDeclaringClass().equals(packet.getClass())) {
                continue;
            }
            try {
                field.setAccessible(true);
                Object value = field.get(packet);
                builder.append(field.getName()).append('=');
                appendValue(value, builder);
                builder.append(' ');
            } catch (IllegalAccessException ignored) {
            }
        }
        return builder.toString();
    }
    private static void appendValue(Object value, StringBuilder builder) {
        if (value == null) {
            builder.append("null");
            return;
        }
        if (value instanceof Optional<?> optional) {
            if (optional.isPresent()) {
                appendValue(optional.get(), builder);
            } else {
                builder.append("Optional.empty");
            }
            return;
        }
        if (value instanceof Map<?, ?> map) {
            appendMap(map, builder);
            return;
        }
        Class<?> valueClass = value.getClass();
        if (valueClass.isArray()) {
            appendArray(value, builder);
            return;
        }
        builder.append(value);
    }
    private static void appendMap(Map<?, ?> map, StringBuilder builder) {
        builder.append('{');
        Iterator<? extends Map.Entry<?, ?>> iterator = map.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<?, ?> entry = iterator.next();
            appendValue(entry.getKey(), builder);
            builder.append('=');
            appendValue(entry.getValue(), builder);
            if (iterator.hasNext()) {
                builder.append(", ");
            }
        }
        builder.append('}');
    }
    private static void appendArray(Object array, StringBuilder builder) {
        builder.append('[');
        int length = Array.getLength(array);
        for (int i = 0; i < length; i++) {
            appendValue(Array.get(array, i), builder);
            if (i + 1 < length) {
                builder.append(", ");
            }
        }
        builder.append(']');
    }
    public static void g8(String value) {
        legacyState = value;
    }
    public static String lo() {
        return legacyState;
    }
}
