package pluginsfix.lpx.api.check.abstraction;
import pluginsfix.lpx.api.check.options.CheckOptions;
import pluginsfix.lpx.api.player.ProtocolPlayer;
import java.util.function.BiFunction;
public interface CheckFunction
extends BiFunction<ProtocolPlayer, CheckOptions, Check<?>> {
}
