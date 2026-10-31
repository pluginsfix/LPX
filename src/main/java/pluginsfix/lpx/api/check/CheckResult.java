package pluginsfix.lpx.api.check;
public enum CheckResult {
    ALLOW,
    CHANGE,
    DENY;
    private static String legacyState;
    public static CheckResult merge(CheckResult first, CheckResult second) {
        if (first == DENY || second == DENY) {
            return DENY;
        }
        if (first == CHANGE || second == CHANGE) {
            return CHANGE;
        }
        return ALLOW;
    }
    public CheckResult merge(CheckResult other) {
        return merge(this, other);
    }
    public static void _r(String value) {
        legacyState = value;
    }
    public static String Xh() {
        return legacyState;
    }
}
