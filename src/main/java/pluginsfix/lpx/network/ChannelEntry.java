package pluginsfix.lpx.network;

public final class ChannelEntry {
    private final String threadName;
    private volatile long lastTickAt;
    private volatile long lastReportAt;

    public ChannelEntry(String threadName) {
        this.threadName = threadName;
    }

    public long getLastTickAt() {
        return lastTickAt;
    }

    public void setLastTickAt(long lastTickAt) {
        this.lastTickAt = lastTickAt;
    }

    public long getLastReportAt() {
        return lastReportAt;
    }

    public void setLastReportAt(long lastReportAt) {
        this.lastReportAt = lastReportAt;
    }

    public String getThreadName() {
        return threadName;
    }

    public long B3(Object[] ignored) {
        return getLastTickAt();
    }

    public void l2(Object[] args) {
        setLastTickAt((Long) args[0]);
    }

    public long Bc(Object[] ignored) {
        return getLastReportAt();
    }

    public void lD(Object[] args) {
        setLastReportAt((Long) args[0]);
    }

    public String B4(Object[] ignored) {
        return getThreadName();
    }
}
