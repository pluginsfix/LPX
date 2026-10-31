package pluginsfix.lpx.util;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
public final class TpsParser {
    private static final Pattern INTEGER_PATTERN = Pattern.compile("-?\\d+");
    private TpsParser() {
    }
    public static int parseLastInteger(String input) {
        if (input == null || input.isEmpty()) {
            return -1;
        }
        Matcher matcher = INTEGER_PATTERN.matcher(input);
        int parsed = -1;
        while (matcher.find()) {
            try {
                parsed = Integer.parseInt(matcher.group());
            } catch (NumberFormatException ignored) {
            }
        }
        return parsed;
    }
    public static int Zu(Object[] args) {
        String input = (String) args[0];
        return parseLastInteger(input);
    }
}
