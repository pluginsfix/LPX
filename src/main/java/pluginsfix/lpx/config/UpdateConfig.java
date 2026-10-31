package pluginsfix.lpx.config;
import pluginsfix.lpx.api.LPXPlugin;
import java.util.List;
public class UpdateConfig extends ConfigSection {
    private final boolean D;
    private final String q;
    private final String v;
    private final List<String> s;
    public UpdateConfig(MessagesConfig messagesConfig, LPXPlugin plugin, long ignored) {
        super(plugin, "update", messagesConfig);
        this.D = ln(new Object[]{"enabled"});
        this.q = lp(new Object[]{0L, "url"});
        this.v = sl(new Object[0]).getString(i + ".embed-color", "#ff0000");
        this.s = lt(new Object[]{0L, "mentions"});
    }
    public boolean p_(Object[] args) {
        return D;
    }
    public String pY(Object[] args) {
        return q;
    }
    public String po(Object[] args) {
        return v;
    }
    @SuppressWarnings("rawtypes")
    public List pq(Object[] args) {
        return s;
    }
}
