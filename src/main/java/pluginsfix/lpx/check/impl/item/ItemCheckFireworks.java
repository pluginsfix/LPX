package pluginsfix.lpx.check.impl.item;
import pluginsfix.lpx.api.check.CheckResult;
import pluginsfix.lpx.api.check.FlagInfo;
import pluginsfix.lpx.api.check.options.CheckOptions;
import pluginsfix.lpx.api.player.ProtocolPlayer;
import pluginsfix.lpx.check.base.AbstractCheckT;
import com.github.retrooper.packetevents.event.simple.ItemStackDecodeEvent;
import com.github.retrooper.packetevents.protocol.component.builtin.item.FireworkExplosion;
import com.github.retrooper.packetevents.protocol.component.builtin.item.ItemFireworks;
import com.github.retrooper.packetevents.protocol.component.ComponentTypes;
import com.github.retrooper.packetevents.protocol.item.ItemStack;
import com.github.retrooper.packetevents.protocol.nbt.NBT;
import com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import com.github.retrooper.packetevents.protocol.nbt.NBTIntArray;
import com.github.retrooper.packetevents.protocol.nbt.NBTList;
public class ItemCheckFireworks extends AbstractCheckT<ItemStackDecodeEvent> {
    private int a;
    private int z;
    public ItemCheckFireworks(ProtocolPlayer player, CheckOptions options) {
        super(player, options);
    }
    @Override
    public void loadOptions() {
        this.a = j_(new Object[0]).getInt(kD(new Object[]{"max-explosions", 0L}), 64);
        this.z = j_(new Object[0]).getInt(kD(new Object[]{"max-colors", 0L}), 64);
    }
    @Override
    protected CheckResult Bu(Object[] args) {
        ProtocolPlayer player = (ProtocolPlayer) args[1];
        ItemStackDecodeEvent event = (ItemStackDecodeEvent) args[2];
        ItemStack itemStack = event.getItemStack();
        if (itemStack == null) {
            return CheckResult.ALLOW;
        }
        if (player.usesComponents()) {
            return validateUsingComponents(itemStack, event);
        }
        return validateUsingLegacyNbt(itemStack, event);
    }
    private CheckResult validateUsingComponents(ItemStack itemStack, ItemStackDecodeEvent event) {
        FireworkExplosion singleExplosion = itemStack.getComponentOr(ComponentTypes.FIREWORK_EXPLOSION, null);
        if (singleExplosion != null) {
            return validateExplosion(singleExplosion, event);
        }
        ItemFireworks fireworks = itemStack.getComponentOr(ComponentTypes.FIREWORKS, null);
        if (fireworks == null) {
            return CheckResult.ALLOW;
        }
        if (fireworks.getExplosions().size() > a) {
            AZ(new Object[]{
                event,
                0L,
                new FlagInfo[]{FlagInfo.of("explosions", fireworks.getExplosions().size())}
            });
            return CheckResult.DENY;
        }
        for (FireworkExplosion explosion : fireworks.getExplosions()) {
            CheckResult result = validateExplosion(explosion, event);
            if (result == CheckResult.DENY) {
                return result;
            }
        }
        return CheckResult.ALLOW;
    }
    private CheckResult validateExplosion(FireworkExplosion explosion, ItemStackDecodeEvent event) {
        if (explosion.getColors().size() > z) {
            AZ(new Object[]{
                event,
                0L,
                new FlagInfo[]{FlagInfo.of("colors", explosion.getColors().size())}
            });
            return CheckResult.DENY;
        }
        if (explosion.getFadeColors().size() > z) {
            AZ(new Object[]{
                event,
                0L,
                new FlagInfo[]{FlagInfo.of("fade-colors", explosion.getFadeColors().size())}
            });
            return CheckResult.DENY;
        }
        return CheckResult.ALLOW;
    }
    @SuppressWarnings("unchecked")
    private CheckResult validateUsingLegacyNbt(ItemStack itemStack, ItemStackDecodeEvent event) {
        NBTCompound root = itemStack.getNBT();
        if (root == null) {
            return CheckResult.ALLOW;
        }
        NBTCompound fireworks = root.getCompoundTagOrNull("Fireworks");
        if (fireworks != null) {
            NBTList<NBTCompound> explosions = fireworks.getCompoundListTagOrNull("Explosions");
            if (explosions != null) {
                if (explosions.size() > a) {
                    AZ(new Object[]{
                        event,
                        0L,
                        new FlagInfo[]{FlagInfo.of("explosions", explosions.size())}
                    });
                    return CheckResult.DENY;
                }
                for (NBTCompound explosion : explosions.getTags()) {
                    CheckResult result = validateExplosionCompound(explosion, event);
                    if (result == CheckResult.DENY) {
                        return result;
                    }
                }
            }
        }
        NBTCompound explosion = root.getCompoundTagOrNull("Explosion");
        if (explosion != null) {
            return validateExplosionCompound(explosion, event);
        }
        return CheckResult.ALLOW;
    }
    private CheckResult validateExplosionCompound(NBTCompound explosion, ItemStackDecodeEvent event) {
        CheckResult colorsResult = validateIntArrayTag(explosion, "Colors", event);
        if (colorsResult == CheckResult.DENY) {
            return colorsResult;
        }
        return validateIntArrayTag(explosion, "FadeColors", event);
    }
    private CheckResult validateIntArrayTag(NBTCompound compound, String tagName, ItemStackDecodeEvent event) {
        NBT tag = compound.getTagOrNull(tagName);
        if (!(tag instanceof NBTIntArray intArray)) {
            return CheckResult.ALLOW;
        }
        int length = intArray.getValue().length;
        if (length <= z) {
            return CheckResult.ALLOW;
        }
        AZ(new Object[]{
            event,
            0L,
            new FlagInfo[]{FlagInfo.of(tagName.toLowerCase(), length)}
        });
        return CheckResult.DENY;
    }
    public CheckResult ZW(Object[] args) {
        ProtocolPlayer player = (ProtocolPlayer) args[0];
        ItemStackDecodeEvent event = (ItemStackDecodeEvent) args[1];
        return Bu(new Object[]{0L, player, event});
    }
    public CheckResult Zx(Object[] args) {
        ItemStack itemStack = (ItemStack) args[0];
        ItemStackDecodeEvent event = (ItemStackDecodeEvent) args[1];
        return validateUsingComponents(itemStack, event);
    }
    public CheckResult cr(Object[] args) {
        FireworkExplosion explosion = (FireworkExplosion) args[1];
        ItemStackDecodeEvent event = (ItemStackDecodeEvent) args[2];
        return validateExplosion(explosion, event);
    }
    public CheckResult Z_(Object[] args) {
        ItemStack itemStack = (ItemStack) args[1];
        ItemStackDecodeEvent event = (ItemStackDecodeEvent) args[2];
        return validateUsingLegacyNbt(itemStack, event);
    }
    public CheckResult mt(Object[] args) {
        NBTCompound explosion = (NBTCompound) args[1];
        ItemStackDecodeEvent event = (ItemStackDecodeEvent) args[2];
        return validateExplosionCompound(explosion, event);
    }
    public CheckResult g8(Object[] args) {
        NBTCompound compound = (NBTCompound) args[0];
        String tagName = (String) args[3];
        ItemStackDecodeEvent event = (ItemStackDecodeEvent) args[4];
        return validateIntArrayTag(compound, tagName, event);
    }
    @Override
    public boolean isPreVia() {
        return true;
    }
}
