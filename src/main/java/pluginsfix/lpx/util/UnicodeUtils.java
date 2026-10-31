package pluginsfix.lpx.util;
public class UnicodeUtils {
    private static final int INDEX_NOT_FOUND = -1;
    private UnicodeUtils() {
    }
    public static CharSequence gc(Object[] args) {
        CharSequence value = (CharSequence) args[0];
        int start = (Integer) args[1];
        if (value == null) {
            return null;
        }
        int safeStart = Math.max(0, Math.min(start, value.length()));
        return value.subSequence(safeStart, value.length());
    }
    static int Qq(Object[] args) {
        CharSequence value = (CharSequence) args[1];
        int codePoint = (Integer) args[2];
        int start = (Integer) args[3];
        if (value == null) {
            return INDEX_NOT_FOUND;
        }
        int safeStart = Math.max(0, start);
        return value.toString().indexOf(new String(Character.toChars(codePoint)), safeStart);
    }
    static int F0(Object[] args) {
        CharSequence source = (CharSequence) args[1];
        CharSequence needle = (CharSequence) args[2];
        int start = (Integer) args[3];
        if (source == null || needle == null) {
            return INDEX_NOT_FOUND;
        }
        return source.toString().indexOf(needle.toString(), Math.max(0, start));
    }
    private static boolean VW(Object[] args) {
        char c = (Character) args[0];
        if (c < 32) {
            return false;
        }
        int type = Character.getType(c);
        return type != Character.UNASSIGNED
            && type != Character.CONTROL
            && type != Character.PRIVATE_USE
            && type != Character.SURROGATE;
    }
    public static char[] Sx(Object[] args) {
        CharSequence source = (CharSequence) args[1];
        if (source == null) {
            return new char[0];
        }
        StringBuilder out = new StringBuilder(source.length());
        for (int i = 0; i < source.length(); i++) {
            char c = source.charAt(i);
            if (VW(new Object[]{c, 0L})) {
                out.append(c);
            }
        }
        return out.toString().toCharArray();
    }
    private static RuntimeException a(RuntimeException runtimeException) {
        return runtimeException;
    }
}
