package pluginsfix.lpx.config;
import pluginsfix.lpx.api.LPXPlugin;
public class PrinterConfig extends ConfigSection {
    private final boolean K;
    private final int z;
    private final int r;
    private final boolean v;
    private final String f;
    private final String a;
    private final String disableMessage;
    public PrinterConfig(long ignored, LPXPlugin plugin, String basePath, MessagesConfig messagesConfig) {
        super(plugin, basePath + ".printer", messagesConfig);
        this.K = ln(new Object[]{"enabled"});
        this.z = readInt("max-blocks", "max", 20);
        this.r = readInt("auto-disable-after-ms", "timeout", 5000);
        this.v = GE(new Object[]{"notify", true});
        this.f = readString("permission", "lpx.notify.printer");
        this.a = readString("message-enable", "&aPrinter mode enabled (%printer-mode%)");
        this.disableMessage = readString("message-disable", "&cPrinter mode disabled");
    }
    public boolean xk(Object[] args) {
        return K;
    }
    public int x2(Object[] args) {
        return z;
    }
    public int xQ(Object[] args) {
        return r;
    }
    public boolean xE(Object[] args) {
        return v;
    }
    public String xm(Object[] args) {
        return f;
    }
    public String xB(Object[] args) {
        return a;
    }
    public String xO(Object[] args) {
        return disableMessage;
    }
    private int readInt(String primary, String legacy, int fallback) {
        String primaryPath = i + "." + primary;
        if (sl(new Object[0]).contains(primaryPath)) {
            return sl(new Object[0]).getInt(primaryPath, fallback);
        }
        return sl(new Object[0]).getInt(i + "." + legacy, fallback);
    }
    private String readString(String key, String fallback) {
        return sl(new Object[0]).getString(i + "." + key, fallback);
    }
}
