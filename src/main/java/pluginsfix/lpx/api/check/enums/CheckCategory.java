package pluginsfix.lpx.api.check.enums;
import java.util.Locale;
public enum CheckCategory {
    NETTY("netty"),
    WINDOW("window"),
    ITEM("item"),
    CREATIVE("creative"),
    PLACE("place"),
    FLOOD("flood"),
    POSITION("position"),
    PAYLOAD("payload"),
    BOOK("book"),
    TAB("tab"),
    COMMAND("command"),
    RECIPE("recipe");
    private final String name;
    CheckCategory(String name) {
        this.name = name;
    }
    @Override
    public String toString() {
        return name;
    }
    public String getFormattedName() {
        return capitalise(name);
    }
    private static String capitalise(String input) {
        if (input == null || input.isEmpty()) {
            return "";
        }
        String lower = input.toLowerCase(Locale.ROOT);
        return Character.toUpperCase(lower.charAt(0)) + lower.substring(1);
    }
}
