package pluginsfix.lpx.check.base;
import pluginsfix.lpx.api.check.CheckResult;
import pluginsfix.lpx.api.check.FlagInfo;
import pluginsfix.lpx.api.check.options.CheckOptions;
import pluginsfix.lpx.api.player.ProtocolPlayer;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;
public abstract class AbstractRecipeCheckT<T> extends AbstractCheckT<T> {
    private static final List<?> B = resolveRecipeList();
    private static int[] G;
    public AbstractRecipeCheckT(ProtocolPlayer player, CheckOptions options) {
        super(player, options);
    }
    @Override
    @SuppressWarnings("unchecked")
    public CheckResult Bu(Object[] args) {
        T packet = (T) args[2];
        if (B == null || B.isEmpty()) {
            return CheckResult.ALLOW;
        }
        int recipeId = F6(new Object[]{packet});
        int recipeCount = B.size();
        if (recipeId < 0 || recipeId >= recipeCount) {
            AZ(
                packet,
                0L,
                new FlagInfo[]{
                    FlagInfo.of("recipe-id", recipeId),
                    FlagInfo.of("recipe-count", recipeCount)
                }
            );
            return CheckResult.DENY;
        }
        return CheckResult.ALLOW;
    }
    public abstract int F6(Object[] args);
    private static List<?> resolveRecipeList() {
        try {
            Class<?> bukkitClass = Class.forName("org.bukkit.Bukkit");
            Method getServer = bukkitClass.getMethod("getServer");
            Object server = getServer.invoke(null);
            if (server == null) {
                return null;
            }
            Method recipeIterator = server.getClass().getMethod("recipeIterator");
            Object iterator = recipeIterator.invoke(server);
            if (iterator instanceof java.util.Iterator<?> it) {
                java.util.ArrayList<Object> recipes = new java.util.ArrayList<>();
                it.forEachRemaining(recipes::add);
                return recipes;
            }
        } catch (Throwable ignored) {
        }
        return Collections.emptyList();
    }
    public static void AW(int[] values) {
        G = values;
    }
    public static int[] A4() {
        return G;
    }
    private static RuntimeException a(RuntimeException runtimeException) {
        return runtimeException;
    }
}
