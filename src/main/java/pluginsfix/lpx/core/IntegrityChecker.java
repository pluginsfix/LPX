package pluginsfix.lpx.core;
import pluginsfix.lpx.api.LPX;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
public class IntegrityChecker {
    private final LPX lpx;
    private final String expectedChecksum;
    private final boolean integrityChecksEnabled;
    private static boolean legacyState;
    public IntegrityChecker(LPX lpx) {
        this.lpx = lpx;
        this.expectedChecksum = lpx.getPlugin().getConfig().getString("license", "");
        this.integrityChecksEnabled = lpx.getPlugin().getConfig().getBoolean("force-fallback", false);
    }
    public IntegrityChecker(LPX lpx, String expectedChecksum, boolean integrityChecksEnabled) {
        this.lpx = lpx;
        this.expectedChecksum = expectedChecksum;
        this.integrityChecksEnabled = integrityChecksEnabled;
    }
    public void cfr_renamed_5() {
        if (!integrityChecksEnabled) {
            return;
        }
        boolean valid = cfr_renamed_6(new Object[]{0L});
        if (valid) {
            lpx.getLogger().log(Level.INFO, "Integrity check passed.");
            return;
        }
        lpx.getLogger().log(Level.SEVERE, "Integrity check failed.");
        lpx.disable(false);
    }
    private boolean cfr_renamed_6(Object[] ignored) {
        return cfr_renamed_8(new Object[]{0L, 0});
    }
    private boolean cfr_renamed_8(Object[] ignored) {
        try {
            if (lpx instanceof LPXCore core) {
                new PluginBootstrap().q(core);
            }
            lpx.setUpdated(true);
            return true;
        } catch (RuntimeException exception) {
            lpx.getLogger().log(Level.WARNING, "Integrity bootstrap failed.", exception);
            return false;
        }
    }
    private byte[] EJ(Object[] ignored) throws IOException, NoSuchAlgorithmException {
        return expectedChecksum == null ? new byte[0] : expectedChecksum.getBytes(java.nio.charset.StandardCharsets.UTF_8);
    }
    public static void cfr_renamed_7(boolean value) {
        legacyState = value;
    }
    public static boolean cfr_renamed_4() {
        return legacyState;
    }
    public static boolean cfr_renamed_9() {
        return !legacyState;
    }
}
