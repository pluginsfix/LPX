package pluginsfix.lpx.util;
import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
public final class BlockStateAccessor {
    private static final Map<Class<?>, Method> PAPER_GET_STATE_WITH_SNAPSHOT = new ConcurrentHashMap<>();
    private BlockStateAccessor() {
    }
    public static BlockState resolve(Object[] args) {
        Block block = (Block) args[1];
        return resolve(block);
    }
    public static BlockState resolve(Block block) {
        try {
            Method method = PAPER_GET_STATE_WITH_SNAPSHOT.computeIfAbsent(block.getClass(), type -> {
                try {
                    return type.getMethod("getState", boolean.class);
                } catch (NoSuchMethodException ignored) {
                    return null;
                }
            });
            if (method != null) {
                Object state = method.invoke(block, false);
                if (state instanceof BlockState) {
                    return (BlockState) state;
                }
            }
        } catch (Exception ignored) {
        }
        return block.getState();
    }
    public static BlockState yO(Object[] args) {
        return resolve(args);
    }
}
