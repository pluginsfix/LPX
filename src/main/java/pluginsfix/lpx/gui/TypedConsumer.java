package pluginsfix.lpx.gui;
import java.util.function.Consumer;
public class TypedConsumer<T> {
    private final Class<T> eventType;
    private final Consumer<T> consumer;
    public TypedConsumer(Class<T> eventType, Consumer<T> consumer) {
        this.eventType = eventType;
        this.consumer = consumer;
    }
    public void accept(Object[] args) {
        Object value = args[0];
        consumer.accept(eventType.cast(value));
    }
    public Class<T> getEventType() {
        return eventType;
    }
    public void pr(Object[] args) {
        accept(args);
    }
    public Class Fn(Object[] ignored) {
        return getEventType();
    }
}
