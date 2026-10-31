package pluginsfix.lpx.check.support;
public enum GameModeFilter {
    CREATIVE,
    ALL,
    SMART,
    NONE;
    public static final GameModeFilter DEFAULT = SMART;
    private static boolean legacyState;
    public static GameModeFilter si(Object[] args) {
        String value = (String) args[0];
        try {
            return GameModeFilter.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException ignored) {
            return DEFAULT;
        }
    }
    public static void Gz(boolean value) {
        legacyState = value;
    }
    public static boolean pF() {
        return legacyState;
    }
    public static boolean pf() {
        return !legacyState;
    }
}
