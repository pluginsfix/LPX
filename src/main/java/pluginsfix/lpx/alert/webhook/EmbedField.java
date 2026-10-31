package pluginsfix.lpx.alert.webhook;
import java.awt.Color;
import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;
import java.util.List;
public class EmbedField {
    private final List<EmbedCondition> j = new ArrayList<>();
    private String l;
    private String q;
    private String D;
    private Color C;
    private WebhookAuth b;
    private String I;
    private EmbedThumbnail E;
    private EmbedFooter W;
    private FieldUrls u;
    public EmbedField() {
    }
    public String Qk(Object[] ignored) {
        return l;
    }
    public EmbedField ql(Object[] args) {
        this.l = (String) args[0];
        return this;
    }
    public String Q1(Object[] ignored) {
        return q;
    }
    public EmbedField q7(Object[] args) {
        String raw = (String) args[1];
        this.q = raw
            .replace("\r\n", "\n")
            .replace("\r", "\n")
            .replace("\n", "\\n")
            .replace("_", "\\_");
        return this;
    }
    public String QX(Object[] ignored) {
        return D;
    }
    public EmbedField qs(Object[] args) {
        this.D = (String) args[0];
        return this;
    }
    public Color Qm(Object[] ignored) {
        return C;
    }
    public EmbedField ml(Object[] args) {
        this.C = (Color) args[0];
        return this;
    }
    public WebhookAuth Q_(Object[] ignored) {
        return b;
    }
    public String QM(Object[] ignored) {
        return I;
    }
    public EmbedField _J(Object[] args) {
        TemporalAccessor temporal = (TemporalAccessor) args[0];
        this.I = temporal.toString();
        return this;
    }
    public EmbedThumbnail QK(Object[] ignored) {
        return E;
    }
    public EmbedField qv(Object[] args) {
        String url = (String) args[0];
        this.E = new EmbedThumbnail(url);
        return this;
    }
    public EmbedFooter Qb(Object[] ignored) {
        return W;
    }
    public EmbedField qG(Object[] args) {
        String text = (String) args[0];
        this.W = new EmbedFooter(text);
        return this;
    }
    public FieldUrls Qy(Object[] ignored) {
        return u;
    }
    @SuppressWarnings("rawtypes")
    public List QB(Object[] ignored) {
        return j;
    }
    public EmbedField RE(Object[] args) {
        String username = (String) args[0];
        String avatarUrl = (String) args[1];
        this.b = new WebhookAuth(username, avatarUrl);
        return this;
    }
    public EmbedField Fv(Object[] args) {
        String titleUrl = (String) args[0];
        String descriptionUrl = (String) args[1];
        String authorUrl = (String) args[2];
        this.u = new FieldUrls(titleUrl, descriptionUrl, authorUrl);
        return this;
    }
    public EmbedField Ue(Object[] args) {
        String key = (String) args[0];
        String expectedValue = (String) args[1];
        boolean equals = (Boolean) args[2];
        this.j.add(new EmbedCondition(key, expectedValue, equals));
        return this;
    }
}
