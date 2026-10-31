package pluginsfix.lpx.api.check;

public final class FlagInfo {
    private final String name;
    private final Object value;

    public FlagInfo(String name, Object value) {
        this.name = name;
        this.value = value;
    }

    public static FlagInfo of(String name, Object value) {
        return new FlagInfo(name, value);
    }

    public String getName() {
        return name;
    }

    public Object getValue() {
        return value;
    }

    @Override
    public String toString() {
        return name + "=" + String.valueOf(value);
    }
}
