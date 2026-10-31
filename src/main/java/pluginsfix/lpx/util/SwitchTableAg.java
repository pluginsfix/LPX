package pluginsfix.lpx.util;
import pluginsfix.lpx.check.support.GameModeFilter;
class SwitchTableAg {
    static final int[] x = new int[GameModeFilter.values().length];
    static {
        try {
            SwitchTableAg.x[GameModeFilter.NONE.ordinal()] = 1;
        }
        catch (NoSuchFieldError noSuchFieldError) {
        }
        try {
            SwitchTableAg.x[GameModeFilter.SMART.ordinal()] = 2;
        }
        catch (NoSuchFieldError noSuchFieldError) {
        }
        try {
            SwitchTableAg.x[GameModeFilter.ALL.ordinal()] = 3;
        }
        catch (NoSuchFieldError noSuchFieldError) {
        }
        try {
            SwitchTableAg.x[GameModeFilter.CREATIVE.ordinal()] = 4;
        }
        catch (NoSuchFieldError noSuchFieldError) {
        }
    }
}
