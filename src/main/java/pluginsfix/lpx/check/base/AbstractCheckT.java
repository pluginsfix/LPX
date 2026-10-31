package pluginsfix.lpx.check.base;
import pluginsfix.lpx.api.check.abstraction.Check;
import pluginsfix.lpx.api.check.CheckResult;
import pluginsfix.lpx.api.check.enums.CheckType;
import pluginsfix.lpx.api.check.FlagInfo;
import pluginsfix.lpx.api.check.options.CheckOptions;
import pluginsfix.lpx.api.check.options.PersonalCheckOptions;
import pluginsfix.lpx.api.player.ProtocolPlayer;
import com.github.retrooper.packetevents.protocol.component.builtin.item.ItemBees;
import com.github.retrooper.packetevents.protocol.component.ComponentTypes;
import com.github.retrooper.packetevents.protocol.item.ItemStack;
import com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import java.util.function.Consumer;
import org.bukkit.configuration.file.FileConfiguration;
public abstract class AbstractCheckT<T> implements Check<T> {
    private static final String OPTIONS_PATH_SEPARATOR = "options.";
    protected final ProtocolPlayer F;
    protected final PersonalCheckOptions Q;
    private static int X = 27;
    protected AbstractCheckT(ProtocolPlayer player, CheckOptions options) {
        this.F = player;
        this.Q = new PersonalCheckOptions(options);
        this.loadOptions();
    }
    private void AG(Object... args) {
        Object packet = args[0];
        FlagInfo[] infos = (FlagInfo[]) args[1];
        F.flag(this, packet, infos);
    }
    protected boolean AZ(Object... args) {
        Object packet = args[0];
        FlagInfo[] infos = (FlagInfo[]) args[2];
        Q.increaseVl();
        Q.setBuffer(Q.getBuffer() * Q.getCheckOptions().getBufferMultiplier());
        AG(packet, infos);
        if (Q.getVl() >= Q.getCheckOptions().getMaxVL()) {
            if (Q.getCheckOptions().isPunish()) {
                F.punish(this);
            }
            Q.setVl(0);
            return true;
        }
        return false;
    }
    protected boolean AH(Object... args) {
        Object packet = args[2];
        FlagInfo[] infos = (FlagInfo[]) args[3];
        return At(0L, packet, 1.0D, infos);
    }
    protected boolean At(Object... args) {
        Object packet = args[1];
        double amount = (Double) args[2];
        FlagInfo[] infos = (FlagInfo[]) args[3];
        double nextBuffer = Math.min(Q.getCheckOptions().getMaxBuffer(), Q.getBuffer() + amount);
        Q.setBuffer(nextBuffer);
        if (nextBuffer >= Q.getCheckOptions().getMaxBuffer()) {
            AZ(packet, 0L, infos);
            return true;
        }
        return false;
    }
    protected void jv(Object[] args) {
        if (Q.getBuffer() <= 0.0D) {
            return;
        }
        Q.setBuffer(Math.max(0.0D, Q.getBuffer() - Q.getCheckOptions().getBufferDecay()));
    }
    protected abstract CheckResult Bu(Object[] args);
    @Override
    public void loadOptions() {
    }
    @Override
    public void reset() {
        Q.reset();
    }
    @Override
    public CheckResult handle(T packet) {
        if (!Q.getCheckOptions().isEnabled()) {
            return CheckResult.ALLOW;
        }
        if (F.ignoreChecks()) {
            return CheckResult.ALLOW;
        }
        return Bu(new Object[]{0L, F, packet});
    }
    @Override
    public PersonalCheckOptions getOptions() {
        return Q;
    }
    @Override
    public CheckType getType() {
        return Q.getCheckOptions().getType();
    }
    protected String kD(Object[] args) {
        String key = (String) args[0];
        return Q.getCheckOptions().getPath() + OPTIONS_PATH_SEPARATOR + key;
    }
    protected FileConfiguration j_(Object[] args) {
        return Q.getCheckOptions().getConfig();
    }
    @SuppressWarnings("unchecked")
    protected void WV(Object[] args) {
        ItemStack itemStack = (ItemStack) args[0];
        Consumer<Object> consumer = (Consumer<Object>) args[1];
        if (itemStack == null) {
            return;
        }
        NBTCompound nbt = itemStack.getNBT();
        if (nbt != null) {
            consumer.accept(nbt);
        }
        itemStack.getComponent(ComponentTypes.CUSTOM_DATA).ifPresent(consumer);
        itemStack.getComponent(ComponentTypes.ENTITY_DATA).ifPresent(consumer);
        itemStack.getComponent(ComponentTypes.BUCKET_ENTITY_DATA).ifPresent(consumer);
        itemStack.getComponent(ComponentTypes.BLOCK_ENTITY_DATA).ifPresent(consumer);
        itemStack.getComponent(ComponentTypes.BEES).ifPresent(bees -> {
            for (ItemBees.BeeEntry bee : bees.getBees()) {
                consumer.accept(bee.getEntityData());
            }
        });
        itemStack.getComponent(ComponentTypes.DEBUG_STICK_STATE).ifPresent(consumer);
    }
    public static void EI(int value) {
        X = value;
    }
    public static int jM() {
        return X;
    }
    public static int jP() {
        return X == 0 ? 27 : 0;
    }
    private static RuntimeException b(RuntimeException runtimeException) {
        return runtimeException;
    }
}
