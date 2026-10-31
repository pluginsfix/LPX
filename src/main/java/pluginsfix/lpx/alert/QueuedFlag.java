package pluginsfix.lpx.alert;

import pluginsfix.lpx.api.check.FlagInfo;
import pluginsfix.lpx.api.check.options.PersonalCheckOptions;

public final class QueuedFlag {
    private final String username;
    private final PersonalCheckOptions options;
    private final long createdAt;
    private final FlagInfo[] infos;

    public QueuedFlag(
        String username,
        PersonalCheckOptions options,
        byte ignoredA,
        long createdAt,
        int ignoredB,
        FlagInfo[] infos,
        int ignoredC
    ) {
        this.username = username;
        this.options = options;
        this.createdAt = createdAt;
        this.infos = infos == null ? new FlagInfo[0] : infos;
    }

    public String getUsername() {
        return username;
    }

    public PersonalCheckOptions getOptions() {
        return options;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public FlagInfo[] getInfos() {
        return infos;
    }

    public String ok(Object[] ignored) {
        return getUsername();
    }

    public PersonalCheckOptions or(Object[] ignored) {
        return getOptions();
    }

    public long oP(Object[] ignored) {
        return getCreatedAt();
    }

    public FlagInfo[] o1(Object[] ignored) {
        return getInfos();
    }
}
