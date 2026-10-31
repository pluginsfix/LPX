package pluginsfix.lpx.api.check;

import pluginsfix.lpx.api.check.abstraction.CheckFunction;
import pluginsfix.lpx.api.check.enums.CheckType;
import pluginsfix.lpx.api.check.options.CheckOptions;

public class RegisteredCheck<T> {
    private final CheckType type;
    private final CheckOptions options;
    private final CheckFunction function;

    public RegisteredCheck(CheckType type, CheckOptions options, CheckFunction function) {
        this.type = type;
        this.options = options;
        this.function = function;
    }

    public CheckType getType() {
        return type;
    }

    public CheckOptions getOptions() {
        return options;
    }

    public CheckFunction getFunction() {
        return function;
    }
}
