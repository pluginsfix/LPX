package pluginsfix.lpx.listeners.impl;

@FunctionalInterface
public interface TickListener {
    void onTick(int tick);

    default void m1(int tick) {
        onTick(tick);
    }
}
