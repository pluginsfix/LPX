package pluginsfix.lpx.tick;

import pluginsfix.lpx.listeners.impl.TickListener;

public interface TickSource extends AutoCloseable {
    void start();

    @Override
    void close();

    int getCurrentTick();

    void addListener(TickListener listener);

    void removeListener(TickListener listener);

    default void dC(Object[] ignored) {
        start();
    }

    default int d8(Object[] ignored) {
        return getCurrentTick();
    }

    default void g2(Object[] args) {
        addListener((TickListener) args[0]);
    }

    default void gu(Object[] args) {
        removeListener((TickListener) args[0]);
    }
}
