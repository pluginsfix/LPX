package pluginsfix.lpx.api.check.abstraction;
import pluginsfix.lpx.api.check.CheckResult;
import pluginsfix.lpx.api.check.enums.CheckType;
import pluginsfix.lpx.api.check.options.PersonalCheckOptions;
public interface Check<T> {
    public CheckType getType();
    public CheckResult handle(T var1);
    public PersonalCheckOptions getOptions();
    default public void reload() {
        reset();
        loadOptions();
    }
    default public boolean isPreVia() {
        return false;
    }
    public void reset();
    public void loadOptions();
}
