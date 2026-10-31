package pluginsfix.lpx.check.dispatch;
import io.netty.buffer.ByteBuf;
import pluginsfix.lpx.api.check.CheckResult;
import pluginsfix.lpx.api.check.CheckResultInfo;
import pluginsfix.lpx.api.player.ProtocolPlayer;
import pluginsfix.lpx.api.protocol.ReceivedPacket;
import pluginsfix.lpx.core.LPXCore;
import com.github.retrooper.packetevents.event.PacketEvent;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.event.simple.ItemStackDecodeEvent;
import com.github.retrooper.packetevents.event.simple.NBTDecodeEvent;
import com.github.retrooper.packetevents.event.simple.PacketPlayReceiveEvent;
import com.github.retrooper.packetevents.event.simple.ReadSlotsEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import com.github.retrooper.packetevents.protocol.player.User;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import pluginsfix.lpx.util.ChannelUtils;
import java.lang.reflect.Constructor;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.HashMap;
import java.util.Map;
public class CheckDispatcher {
    private static final String WRAPPER_PACKAGE =
        "com.github.retrooper.packetevents.wrapper.play.client.";
    protected static final Map<PacketTypeCommon, Function<PacketReceiveEvent, PacketWrapper<?>>> j = new HashMap<>();
    private final LPXCore core;
    public CheckDispatcher(LPXCore core) {
        this.core = core;
    }
    public void handlePacketReceive(PacketReceiveEvent event, boolean preVia) {
        z5(new Object[]{event, preVia, 0L});
    }
    public CheckResult handleExternalEvent(PacketEvent event, boolean preVia) {
        return Jq(new Object[]{0L, event, preVia});
    }
    void z5(Object[] args) {
        PacketReceiveEvent event = (PacketReceiveEvent) args[0];
        boolean preVia = (Boolean) args[1];
        if (event == null) {
            return;
        }
        ProtocolPlayer player = core.getRegistry().getPlayer(event.getUser()).orElse(null);
        if (player == null) {
            return;
        }
        player.incrementPacketCount();
        ensureLastReceivedPacket(event, player);
        if (aO(new Object[]{0L, event, player, ByteBuf.class, event.getByteBuf(), preVia}) == CheckResult.DENY) {
            player.setLastReceivedPacket(null);
            return;
        }
        if (aO(new Object[]{0L, event, player, PacketPlayReceiveEvent.class, event, preVia}) == CheckResult.DENY) {
            player.setLastReceivedPacket(null);
            return;
        }
        PacketTypeCommon normalizedType = normalizeType(event.getPacketType());
        PacketWrapper<?> wrapper = createWrapper(event, normalizedType);
        if (wrapper == null) {
            return;
        }
        ReceivedPacket last = player.getLastReceivedPacket();
        if (last != null && last.getPacket() == null) {
            last.setPacket(wrapper);
        }
        if (event.isCancelled()) {
            player.setLastReceivedPacket(null);
            return;
        }
        if (aO(new Object[]{0L, event, player, normalizedType, wrapper, preVia}) == CheckResult.DENY) {
            player.setLastReceivedPacket(null);
        }
    }
    CheckResult Jq(Object[] args) {
        PacketEvent sourceEvent = (PacketEvent) args[1];
        boolean preVia = (Boolean) args[2];
        if (sourceEvent == null) {
            return CheckResult.ALLOW;
        }
        PacketEvent baseEvent = unwrapEvent(sourceEvent);
        if (!(baseEvent instanceof PacketReceiveEvent)) {
            return CheckResult.ALLOW;
        }
        PacketReceiveEvent receiveEvent = (PacketReceiveEvent) baseEvent;
        if (receiveEvent.isCancelled()) {
            return CheckResult.ALLOW;
        }
        User user = receiveEvent.getUser();
        if (user == null || user.getUUID() == null) {
            return CheckResult.ALLOW;
        }
        ProtocolPlayer player = core.getRegistry().getPlayer(user).orElse(null);
        if (player == null) {
            return CheckResult.ALLOW;
        }
        if (sourceEvent instanceof ItemStackDecodeEvent) {
            return aO(new Object[]{0L, receiveEvent, player, ItemStackDecodeEvent.class, sourceEvent, preVia});
        }
        if (sourceEvent instanceof NBTDecodeEvent) {
            return aO(new Object[]{0L, receiveEvent, player, NBTDecodeEvent.class, sourceEvent, preVia});
        }
        return aO(new Object[]{0L, receiveEvent, player, ReadSlotsEvent.class, sourceEvent, preVia});
    }
    public CheckResult aO(Object[] args) {
        PacketReceiveEvent receiveEvent = (PacketReceiveEvent) args[1];
        ProtocolPlayer player = (ProtocolPlayer) args[2];
        Object checkKey = args[3];
        Object packetObject = args[4];
        boolean preVia = (Boolean) args[5];
        CheckResultInfo resultInfo = player.handleChecks(checkKey, packetObject, preVia);
        CheckResult result = resultInfo.result();
        if (result == CheckResult.DENY) {
            receiveEvent.setCancelled(true);
            if (core.sZ(new Object[0]).Bw(new Object[0]).YR(new Object[0])) {
                String checkTag = resultInfo.lastCheck() == null
                    ? "[UNKNOWN]"
                    : "[" + resultInfo.lastCheck().getType().getId().name() + "]";
                core.s1(new Object[0]).getFileLogger().l1(new Object[]{
                    (Supplier<String>) () -> formatCancelledPacket(checkTag, receiveEvent, player)
                });
            }
        } else if (result == CheckResult.CHANGE) {
            receiveEvent.markForReEncode(true);
        }
        return result;
    }
    private String formatCancelledPacket(String checkTag, PacketReceiveEvent event, ProtocolPlayer player) {
        return checkTag
            + " Cancelled packet "
            + event.getPacketType().getName()
            + " from "
            + player.getUsername();
    }
    private PacketEvent unwrapEvent(PacketEvent event) {
        if (event instanceof ItemStackDecodeEvent) {
            return unwrapEvent(((ItemStackDecodeEvent) event).getEvent());
        }
        if (event instanceof NBTDecodeEvent) {
            return unwrapEvent(((NBTDecodeEvent) event).getEvent());
        }
        if (event instanceof ReadSlotsEvent) {
            return unwrapEvent(((ReadSlotsEvent) event).getEvent());
        }
        return event;
    }
    private void ensureLastReceivedPacket(PacketReceiveEvent event, ProtocolPlayer player) {
        if (player.getLastReceivedPacket() != null) {
            return;
        }
        long tick = 0L;
        try {
            tick = core.sq(new Object[0]).ny(new Object[0]);
        } catch (RuntimeException ignored) {
        }
        player.setLastReceivedPacket(new ReceivedPacket(
            player.getUsername(),
            tick,
            event.getPacketType(),
            ChannelUtils.Xs(new Object[]{event.getByteBuf()}),
            null
        ));
    }
    private PacketWrapper<?> createWrapper(PacketReceiveEvent event, PacketTypeCommon packetType) {
        Function<PacketReceiveEvent, PacketWrapper<?>> factory = j.get(packetType);
        if (factory == null) {
            return null;
        }
        try {
            return factory.apply(event);
        } catch (RuntimeException ignored) {
            return null;
        }
    }
    private static PacketTypeCommon normalizeType(PacketTypeCommon type) {
        if (type == PacketType.Play.Client.PLAYER_POSITION
            || type == PacketType.Play.Client.PLAYER_POSITION_AND_ROTATION
            || type == PacketType.Play.Client.PLAYER_ROTATION) {
            return PacketType.Play.Client.PLAYER_FLYING;
        }
        return type;
    }
    private static PacketWrapper<?> createByName(String wrapperSimpleName, PacketReceiveEvent event) {
        String className = WRAPPER_PACKAGE + wrapperSimpleName;
        try {
            Class<?> wrapperClass = Class.forName(className);
            Constructor<?> constructor = wrapperClass.getConstructor(PacketReceiveEvent.class);
            Object instance = constructor.newInstance(event);
            return (PacketWrapper<?>) instance;
        } catch (ReflectiveOperationException reflectiveOperationException) {
            throw new IllegalStateException("Unable to instantiate wrapper: " + className, reflectiveOperationException);
        }
    }
    private static void registerWrapper(PacketTypeCommon type, String wrapperSimpleName) {
        j.put(type, event -> createByName(wrapperSimpleName, event));
    }
    static {
        registerWrapper(PacketType.Play.Client.CLICK_WINDOW, "WrapperPlayClientClickWindow");
        registerWrapper(PacketType.Play.Client.PICK_ITEM_FROM_BLOCK, "WrapperPlayClientPickItemFromBlock");
        registerWrapper(PacketType.Play.Client.PICK_ITEM_FROM_ENTITY, "WrapperPlayClientPickItemFromEntity");
        registerWrapper(PacketType.Play.Client.CREATIVE_INVENTORY_ACTION, "WrapperPlayClientCreativeInventoryAction");
        registerWrapper(PacketType.Play.Client.PLAYER_BLOCK_PLACEMENT, "WrapperPlayClientPlayerBlockPlacement");
        registerWrapper(PacketType.Play.Client.UPDATE_SIGN, "WrapperPlayClientUpdateSign");
        registerWrapper(PacketType.Play.Client.PLAYER_DIGGING, "WrapperPlayClientPlayerDigging");
        registerWrapper(PacketType.Play.Client.PLAYER_FLYING, "WrapperPlayClientPlayerFlying");
        registerWrapper(PacketType.Play.Client.PLUGIN_MESSAGE, "WrapperPlayClientPluginMessage");
        registerWrapper(PacketType.Play.Client.EDIT_BOOK, "WrapperPlayClientEditBook");
        registerWrapper(PacketType.Play.Client.TAB_COMPLETE, "WrapperPlayClientTabComplete");
        registerWrapper(PacketType.Play.Client.CRAFT_RECIPE_REQUEST, "WrapperPlayClientCraftRecipeRequest");
        registerWrapper(PacketType.Play.Client.SET_DISPLAYED_RECIPE, "WrapperPlayClientSetDisplayedRecipe");
    }
}
