package pluginsfix.lpx.api.check;

import pluginsfix.lpx.api.check.abstraction.Check;
import org.jspecify.annotations.Nullable;

public class CheckResultInfo {
    private final CheckResult result;
    private final @Nullable Check<?> lastCheck;

    public CheckResultInfo(CheckResult result, @Nullable Check<?> lastCheck) {
        this.result = result;
        this.lastCheck = lastCheck;
    }

    public CheckResultInfo(CheckResult result) {
        this(result, null);
    }

    public CheckResult result() {
        return result;
    }

    public CheckResultInfo result(CheckResult newResult) {
        return new CheckResultInfo(newResult, lastCheck);
    }

    public @Nullable Check<?> lastCheck() {
        return lastCheck;
    }

    public CheckResultInfo lastCheck(@Nullable Check<?> newLastCheck) {
        return new CheckResultInfo(result, newLastCheck);
    }
}
