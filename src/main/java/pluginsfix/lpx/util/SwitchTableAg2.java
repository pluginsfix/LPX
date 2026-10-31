package pluginsfix.lpx.util;
import pluginsfix.lpx.check.support.GameModeFilter;
class SwitchTableAg2 {
    static final int[] l = new int[GameModeFilter.values().length];
    static {
        try {
            SwitchTableAg2.l[GameModeFilter.NONE.ordinal()] = 1;
        }
        catch (NoSuchFieldError noSuchFieldError) {
        }
        try {
            SwitchTableAg2.l[GameModeFilter.SMART.ordinal()] = 2;
        }
        catch (NoSuchFieldError noSuchFieldError) {
        }
        try {
            SwitchTableAg2.l[GameModeFilter.ALL.ordinal()] = 3;
        }
        catch (NoSuchFieldError noSuchFieldError) {
        }
        try {
            SwitchTableAg2.l[GameModeFilter.CREATIVE.ordinal()] = 4;
        }
        catch (NoSuchFieldError noSuchFieldError) {
        }
    }
}
