package pluginsfix.lpx.config;
import pluginsfix.lpx.alert.AlertConfig;
import pluginsfix.lpx.api.LPXPlugin;
public class MessagesConfig extends ConfigSection {
    private final String mainColor;
    private final String secondColor;
    private final String prefix;
    private final String kickMessage;
    private final String kickAlertMessage;
    private final String noPermissionMessage;
    private final String invalidArgumentsMessage;
    private final String injectionFailureMessage;
    private final String updateFoundMessage;
    private final String nullAddressMessage;
    private final String unknownCommandMessage;
    private final AlertConfig alertMessages;
    private static String[] legacyState;
    public MessagesConfig(LPXPlugin plugin, long ignored) {
        super(plugin, "messages", null);
        this.mainColor = lp(new Object[]{0L, "main-color"});
        this.secondColor = lp(new Object[]{0L, "second-color"});
        Yv(this);
        this.prefix = lp(new Object[]{0L, "prefix"});
        this.kickMessage = lp(new Object[]{0L, "kick"});
        this.kickAlertMessage = lp(new Object[]{0L, "kick-alert"});
        this.noPermissionMessage = lp(new Object[]{0L, "no-permission"});
        this.invalidArgumentsMessage = lp(new Object[]{0L, "invalid-arguments"});
        this.injectionFailureMessage = lp(new Object[]{0L, "injection-failure"});
        this.updateFoundMessage = lp(new Object[]{0L, "update-found"});
        this.nullAddressMessage = lp(new Object[]{0L, "null-address"});
        this.unknownCommandMessage = lp(new Object[]{0L, "unknown-command"});
        this.alertMessages = new AlertConfig((char) 0, 0, plugin, this, (short) 0, "messages");
    }
    public String getMainColor() {
        return mainColor;
    }
    public String getSecondColor() {
        return secondColor;
    }
    public String getPrefix() {
        return prefix;
    }
    public String getKickMessage() {
        return kickMessage;
    }
    public String getKickAlertMessage() {
        return kickAlertMessage;
    }
    public String getNoPermissionMessage() {
        return noPermissionMessage;
    }
    public String getInvalidArgumentsMessage() {
        return invalidArgumentsMessage;
    }
    public String getInjectionFailureMessage() {
        return injectionFailureMessage;
    }
    public String getUpdateFoundMessage() {
        return updateFoundMessage;
    }
    public String getNullAddressMessage() {
        return nullAddressMessage;
    }
    public String getUnknownCommandMessage() {
        return unknownCommandMessage;
    }
    public AlertConfig getAlertMessages() {
        return alertMessages;
    }
    public String Vj(Object[] ignored) {
        return getMainColor();
    }
    public String Vp(Object[] ignored) {
        return getSecondColor();
    }
    public String V8(Object[] ignored) {
        return getPrefix();
    }
    public String Vv(Object[] ignored) {
        return getKickMessage();
    }
    public String V5(Object[] ignored) {
        return getKickAlertMessage();
    }
    public String VH(Object[] ignored) {
        return getNoPermissionMessage();
    }
    public String Vl(Object[] ignored) {
        return getInvalidArgumentsMessage();
    }
    public String Vw(Object[] ignored) {
        return getInjectionFailureMessage();
    }
    public String Vq(Object[] ignored) {
        return getUpdateFoundMessage();
    }
    public String VN(Object[] ignored) {
        return getNullAddressMessage();
    }
    public String VJ(Object[] ignored) {
        return getUnknownCommandMessage();
    }
    public AlertConfig VM(Object[] ignored) {
        return getAlertMessages();
    }
    public static void Vf(String[] values) {
        legacyState = values;
    }
    public static String[] Vt() {
        return legacyState;
    }
}
