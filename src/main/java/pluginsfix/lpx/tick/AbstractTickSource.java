package pluginsfix.lpx.tick;

import pluginsfix.lpx.listeners.impl.TickListener;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

public abstract class AbstractTickSource implements TickSource {
    private final Set<TickListener> listeners = new CopyOnWriteArraySet<>();
    private int currentTick = 0;

    protected void tick() {
        for (TickListener listener : listeners) {
            listener.onTick(currentTick);
        }
        currentTick++;
    }

    @Override
    public int getCurrentTick() {
        return currentTick;
    }

    @Override
    public void addListener(TickListener listener) {
        listeners.add(listener);
    }

    @Override
    public void removeListener(TickListener listener) {
        listeners.remove(listener);
    }

    protected void tick(Object[] ignored) {
        tick();
    }
}
