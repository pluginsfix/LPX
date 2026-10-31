package pluginsfix.lpx.check.impl.payload;
import io.netty.buffer.Unpooled;
import pluginsfix.lpx.api.check.CheckResult;
import pluginsfix.lpx.api.check.FlagInfo;
import pluginsfix.lpx.api.check.options.CheckOptions;
import pluginsfix.lpx.api.player.ProtocolPlayer;
import pluginsfix.lpx.check.base.AbstractCheckT;
import pluginsfix.lpx.check.manager.CheckManagerImpl;
import com.github.retrooper.packetevents.event.simple.ItemStackDecodeEvent;
import com.github.retrooper.packetevents.event.simple.NBTDecodeEvent;
import com.github.retrooper.packetevents.netty.buffer.ByteBufHelper;
import com.github.retrooper.packetevents.protocol.item.ItemStack;
import com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPluginMessage;
import pluginsfix.lpx.network.PacketFactory;
public class PayloadCheckItemPayload extends AbstractCheckT<WrapperPlayClientPluginMessage> {
    private final boolean preVia;
    private static PacketWrapper[] legacyPacketWrapperCache;
    public PayloadCheckItemPayload(ProtocolPlayer player, CheckOptions options, boolean preVia) {
        super(player, options);
        this.preVia = preVia;
    }
    @Override
    protected CheckResult Bu(Object[] args) {
        return yN(args);
    }
    public CheckResult yN(Object[] args) {
        long token = (Long) args[0];
        ProtocolPlayer protocolPlayer = (ProtocolPlayer) args[1];
        WrapperPlayClientPluginMessage packet = (WrapperPlayClientPluginMessage) args[2];
        if (!CheckManagerImpl.L.contains(packet.getChannelName())) {
            return CheckResult.ALLOW;
        }
        if (!protocolPlayer.hasAnyCheck(ItemStackDecodeEvent.class, preVia)) {
            return CheckResult.ALLOW;
        }
        PacketWrapper reader = null;
        PacketWrapper writer = null;
        protocolPlayer.setProcessingPayload(true);
        try {
            reader = PacketFactory.Sm(new Object[]{
                protocolPlayer,
                token,
                Unpooled.copiedBuffer(packet.getData()),
                preVia
            });
            ItemStack itemStack = reader.readItemStack();
            NBTCompound nbt = itemStack.getNBT();
            CheckResult result = CheckResult.ALLOW;
            if (nbt != null) {
                result = result.merge(
                    protocolPlayer.handleChecks(
                        NBTDecodeEvent.class,
                        new NBTDecodeEvent(protocolPlayer, null, nbt),
                        preVia
                    ).result()
                );
                if (result == CheckResult.DENY) {
                    return CheckResult.DENY;
                }
            }
            result = result.merge(
                protocolPlayer.handleChecks(
                    ItemStackDecodeEvent.class,
                    new ItemStackDecodeEvent(protocolPlayer, null, itemStack),
                    preVia
                ).result()
            );
            if (result == CheckResult.CHANGE) {
                writer = PacketFactory.Sm(new Object[]{
                    protocolPlayer,
                    token,
                    Unpooled.buffer(),
                    preVia
                });
                writer.writeItemStack(itemStack);
                ByteBufHelper.capacity(writer.buffer, ByteBufHelper.writerIndex(writer.buffer));
                packet.setData(ByteBufHelper.array(writer.buffer));
            }
            return result;
        } catch (Throwable throwable) {
            AZ(new Object[]{
                packet,
                token,
                new FlagInfo[]{FlagInfo.of("exception", throwable.getMessage())}
            });
            return CheckResult.DENY;
        } finally {
            protocolPlayer.setProcessingPayload(false);
            PacketFactory.Ms(token, reader);
            PacketFactory.Ms(token, writer);
        }
    }
    @Override
    public boolean isPreVia() {
        return preVia;
    }
    public static void sJ(PacketWrapper[] value) {
        legacyPacketWrapperCache = value;
    }
    public static PacketWrapper[] Wx() {
        return legacyPacketWrapperCache;
    }
}
