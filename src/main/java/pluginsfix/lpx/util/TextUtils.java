package pluginsfix.lpx.util;
public final class TextUtils {
    private static boolean legacyState = true;
    private TextUtils() {
    }
    public static int t4(Object[] args) {
        CharSequence value = (CharSequence) args[1];
        return value == null ? 0 : value.length();
    }
    public static boolean yP(Object[] args) {
        CharSequence source = (CharSequence) args[0];
        CharSequence prefix = (CharSequence) args[2];
        return C7(new Object[]{source, 0L, prefix, true});
    }
    public static boolean yU(Object[] args) {
        CharSequence source = (CharSequence) args[0];
        CharSequence needle = (CharSequence) args[2];
        return contains(source, needle, true);
    }
    public static int yc(Object[] args) {
        CharSequence source = (CharSequence) args[0];
        CharSequence needle = (CharSequence) args[1];
        if (tr(new Object[]{0L, source}) || tr(new Object[]{0L, needle})) {
            return 0;
        }
        String text = source.toString();
        String search = needle.toString();
        int count = 0;
        int fromIndex = 0;
        while (true) {
            int index = text.indexOf(search, fromIndex);
            if (index == -1) {
                break;
            }
            count++;
            fromIndex = index + search.length();
        }
        return count;
    }
    public static boolean tr(Object[] args) {
        CharSequence value = (CharSequence) args[1];
        return value == null || value.length() == 0;
    }
    private static boolean C7(Object[] args) {
        CharSequence source = (CharSequence) args[0];
        CharSequence prefix = (CharSequence) args[2];
        boolean ignoreCase = (Boolean) args[3];
        if (source == null || prefix == null) {
            return source == prefix;
        }
        if (prefix.length() > source.length()) {
            return false;
        }
        return regionMatches(source, 0, prefix, 0, prefix.length(), ignoreCase);
    }
    public static void gf(boolean value) {
        legacyState = value;
    }
    public static boolean Ok() {
        return legacyState;
    }
    public static boolean OG() {
        return !Ok();
    }
    private static boolean contains(CharSequence source, CharSequence needle, boolean ignoreCase) {
        if (source == null || needle == null) {
            return source == needle;
        }
        int sourceLength = source.length();
        int needleLength = needle.length();
        if (needleLength == 0) {
            return true;
        }
        if (needleLength > sourceLength) {
            return false;
        }
        for (int i = 0; i <= sourceLength - needleLength; i++) {
            if (regionMatches(source, i, needle, 0, needleLength, ignoreCase)) {
                return true;
            }
        }
        return false;
    }
    private static boolean regionMatches(
        CharSequence left,
        int leftOffset,
        CharSequence right,
        int rightOffset,
        int length,
        boolean ignoreCase
    ) {
        String leftString = left.toString();
        String rightString = right.toString();
        return leftString.regionMatches(ignoreCase, leftOffset, rightString, rightOffset, length);
    }
}
