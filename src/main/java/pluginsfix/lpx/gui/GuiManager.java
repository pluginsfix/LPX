package pluginsfix.lpx.gui;

import pluginsfix.lpx.core.LPXCore;

public class GuiManager {
    private final LPXCore core;
    private final GuiInventory mainGuiInventory;
    private static boolean debugFlag;

    public GuiManager(LPXCore core, long ignored) {
        this.core = core;
        this.mainGuiInventory = new MainGui(core).s_(new Object[]{0L});
    }

    public GuiInventory createCheckSettingsGui() {
        return new CheckSettingsGui(0L, core).f5(new Object[]{0L});
    }

    public GuiInventory getMainGuiInventory() {
        return mainGuiInventory;
    }

    public static void setDebugFlag(boolean enabled) {
        debugFlag = enabled;
    }

    public static boolean isDebugFlagEnabled() {
        return debugFlag;
    }

    public static boolean isDebugFlagDisabled() {
        return !isDebugFlagEnabled();
    }

    public GuiInventory Od(Object[] ignored) {
        return createCheckSettingsGui();
    }

    public GuiInventory Ou(Object[] ignored) {
        return getMainGuiInventory();
    }

    public static void cf(boolean value) {
        setDebugFlag(value);
    }

    public static boolean OP() {
        return isDebugFlagEnabled();
    }

    public static boolean OZ() {
        return isDebugFlagDisabled();
    }
}
