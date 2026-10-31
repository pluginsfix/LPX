package pluginsfix.lpx.network;
import pluginsfix.lpx.api.player.ProtocolPlayer;
import pluginsfix.lpx.check.dispatch.CheckDispatcher;
import pluginsfix.lpx.config.ItemStripConfig;
import pluginsfix.lpx.core.LPXCore;
import com.github.retrooper.packetevents.event.PacketEvent;
import com.github.retrooper.packetevents.event.PacketListenerAbstract;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.manager.server.VersionComparison;
import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.protocol.component.ComponentType;
import com.github.retrooper.packetevents.protocol.component.PatchableComponentMap;
import com.github.retrooper.packetevents.protocol.item.ItemStack;
import com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.player.User;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerSetSlot;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerTabComplete;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerWindowItems;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
public class PacketHandler extends PacketListenerAbstract {
    private static final String[] COMMAND_HIDE_BYPASS_PERMISSIONS = {
        "lpx.command.bypass",
        "lpx.bypass.command-hide",
        "lpx.commandhide.bypass"
    };
    private final LPXCore core;
    private final CheckDispatcher dispatcher;
    private final boolean legacyTabComplete;
    private final boolean componentPatchApi;
    private static int[] legacyState;
    public PacketHandler(LPXCore core, CheckDispatcher dispatcher, long ignored) {
        this.core = core;
        this.dispatcher = dispatcher;
        this.legacyTabComplete = PacketEvents.getAPI().getServerManager().getVersion()
            .is(VersionComparison.OLDER_THAN_OR_EQUALS, ServerVersion.V_1_12_2);
        this.componentPatchApi = PacketEvents.getAPI().getServerManager().getVersion()
            .isNewerThan(ServerVersion.V_1_20_4);
    }
    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        dispatcher.handlePacketReceive(event, false);
    }
    @Override
    public void onPacketEventExternal(PacketEvent event) {
        dispatcher.handleExternalEvent(event, false);
    }
    @Override
    public void onPacketSend(PacketSendEvent event) {
        ItemStripConfig stripConfig = core.sZ(new Object[0]).Bw(new Object[0]).YT(new Object[0]);
        if (stripConfig.isEnabled()) {
            handleOutgoingItemStrip(stripConfig, event);
        }
        if (!legacyTabComplete) {
            return;
        }
        if (event.getPacketType() != PacketType.Play.Server.TAB_COMPLETE) {
            return;
        }
        User user = event.getUser();
        if (user == null || user.getUUID() == null) {
            return;
        }
        core.getRegistry().getPlayer(user).ifPresent(protocolPlayer -> filterTabComplete(event, protocolPlayer));
    }
    private void filterTabComplete(PacketSendEvent event, ProtocolPlayer protocolPlayer) {
        Player player = protocolPlayer.getPlayer().orElse(null);
        if (player == null || canSeeHiddenCommands(core, player, 0L)) {
            return;
        }
        WrapperPlayServerTabComplete wrapper = new WrapperPlayServerTabComplete(event);
        boolean changed = wrapper.getCommandMatches().removeIf(match -> match.getText().startsWith("/"));
        if (changed) {
            event.markForReEncode(true);
        }
    }
    private void handleOutgoingItemStrip(ItemStripConfig config, PacketSendEvent event) {
        if (event.getPacketType() == PacketType.Play.Server.WINDOW_ITEMS) {
            WrapperPlayServerWindowItems windowItems = new WrapperPlayServerWindowItems(event);
            List<ItemStack> items = windowItems.getItems();
            boolean changed = false;
            for (int i = 0; i < items.size(); i++) {
                ItemStack stripped = stripItem(items.get(i), config);
                if (stripped != null) {
                    items.set(i, stripped);
                    changed = true;
                }
            }
            ItemStack carried = windowItems.getCarriedItem().orElse(null);
            if (carried != null) {
                ItemStack strippedCarried = stripItem(carried, config);
                if (strippedCarried != null) {
                    windowItems.setCarriedItem(strippedCarried);
                    changed = true;
                }
            }
            if (changed) {
                event.markForReEncode(true);
            }
            return;
        }
        if (!config.isSetSlot()) {
            return;
        }
        if (event.getPacketType() != PacketType.Play.Server.SET_SLOT) {
            return;
        }
        WrapperPlayServerSetSlot setSlot = new WrapperPlayServerSetSlot(event);
        ItemStack stripped = stripItem(setSlot.getItem(), config);
        if (stripped != null) {
            setSlot.setItem(stripped);
            event.markForReEncode(true);
        }
    }
    private ItemStack stripItem(ItemStack item, ItemStripConfig config) {
        if (item == null || item.isEmpty()) {
            return null;
        }
        if (componentPatchApi) {
            PatchableComponentMap components = item.getComponents();
            if (components == null) {
                return null;
            }
            Map<ComponentType<?>, Optional<?>> patches = components.getPatches();
            if (patches == null) {
                return null;
            }
            boolean changed = patches.keySet().removeIf(type -> config.getRemoveComponents().contains(type.getName().getKey()));
            return changed ? item : null;
        }
        NBTCompound nbt = item.getNBT();
        if (nbt == null) {
            return null;
        }
        boolean changed = nbt.getTags().keySet().removeIf(config.getRemoveNbt()::contains);
        return changed ? item : null;
    }
    public static boolean canSeeHiddenCommands(LPXCore core, CommandSender sender, long ignored) {
        if (!core.sZ(new Object[0]).Bw(new Object[0]).Yr(new Object[0])) {
            return true;
        }
        if (sender.isOp()) {
            return true;
        }
        for (String permission : COMMAND_HIDE_BYPASS_PERMISSIONS) {
            if (sender.hasPermission(permission)) {
                return true;
            }
        }
        return false;
    }
    private void sQ(Object[] args) {
        ItemStripConfig config = (ItemStripConfig) args[0];
        PacketSendEvent event = (PacketSendEvent) args[2];
        handleOutgoingItemStrip(config, event);
    }
    private ItemStack RR(Object[] args) {
        ItemStack item = (ItemStack) args[0];
        ItemStripConfig config = (ItemStripConfig) args[2];
        return stripItem(item, config);
    }
    public static boolean Qm(Object[] args) {
        LPXCore core = (LPXCore) args[0];
        CommandSender sender = (CommandSender) args[1];
        return canSeeHiddenCommands(core, sender, 0L);
    }
    public static void ko(int[] value) {
        legacyState = value;
    }
    public static int[] rs() {
        return legacyState;
    }
    private static RuntimeException a(RuntimeException runtimeException) {
        return runtimeException;
    }
}
