package pluginsfix.lpx.gui;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

public class ItemBuilder {
    private static final Pattern HEX_PATTERN = Pattern.compile("&#([A-Fa-f0-9]{6})");
    private final ItemStack x;
    private static boolean i;
    private static final short b = Short.MAX_VALUE;

    public ItemBuilder(Material material) {
        this(material, 1);
    }

    public ItemBuilder(ItemStack itemStack) {
        this.x = itemStack;
    }

    public ItemBuilder(Material material, int amount) {
        this.x = new ItemStack(material, amount);
    }

    public static ItemBuilder tC(Object[] args) {
        Material material = (Material) args[0];
        return new ItemBuilder(material);
    }

    public ItemBuilder dL(Object[] args) {
        ItemMeta meta = x.getItemMeta();
        if (meta != null) {
            meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
            x.setItemMeta(meta);
        }
        return this;
    }

    public ItemBuilder Un(Object[] args) {
        int amount = (Integer) args[0];
        x.setAmount(amount);
        return this;
    }

    public ItemBuilder Ef(Object[] args) {
        String displayName = (String) args[1];
        ItemMeta meta = x.getItemMeta();
        if (meta != null && displayName != null) {
            meta.setDisplayName(formatClean(displayName));
            x.setItemMeta(meta);
        }
        return this;
    }

    public ItemBuilder YA(Object[] args) {
        Enchantment enchantment = (Enchantment) args[0];
        int level = (Integer) args[1];
        x.addUnsafeEnchantment(enchantment, level);
        return this;
    }

    public ItemBuilder wx(Object[] args) {
        Enchantment enchantment = (Enchantment) args[0];
        x.removeEnchantment(enchantment);
        return this;
    }

    public ItemBuilder ES(Object[] args) {
        String owner = (String) args[0];
        ItemMeta meta = x.getItemMeta();
        if (meta instanceof SkullMeta skullMeta) {
            skullMeta.setOwner(owner);
            x.setItemMeta(skullMeta);
        }
        return this;
    }

    public ItemBuilder Yq(Object[] args) {
        Enchantment enchantment = (Enchantment) args[1];
        int level = (Integer) args[2];
        if (level <= 0) {
            return this;
        }
        ItemMeta meta = x.getItemMeta();
        if (meta != null) {
            meta.addEnchant(enchantment, level, true);
            x.setItemMeta(meta);
        }
        return this;
    }

    public ItemBuilder du(Object[] args) {
        x.setDurability(b);
        return this;
    }

    public ItemBuilder UN(Object... args) {
        String[] lore = (String[]) args[1];
        return n8(new Object[]{Lists.newArrayList(lore), 0L});
    }

    public ItemBuilder n8(Object[] args) {
        List<String> lore = (List<String>) args[0];
        ItemMeta meta = x.getItemMeta();
        if (meta != null) {
            List<String> formatted = new ArrayList<>();
            formatted.add("");
            if (lore != null) {
                for (String line : lore) {
                    if (line != null) {
                        formatted.add(formatClean(line));
                    }
                }
            }
            meta.setLore(formatted);
            x.setItemMeta(meta);
        }
        return this;
    }

    public ItemStack dk(Object[] args) {
        return x;
    }

    private static String formatClean(String text) {
        if (text == null || text.isEmpty()) {
            return "";
        }
        Matcher matcher = HEX_PATTERN.matcher(text);
        StringBuffer buffer = new StringBuffer();
        while (matcher.find()) {
            String hex = matcher.group(1);
            matcher.appendReplacement(buffer, net.md_5.bungee.api.ChatColor.of("#" + hex).toString());
        }
        matcher.appendTail(buffer);
        String colored = ChatColor.translateAlternateColorCodes('&', buffer.toString());
        return ChatColor.RESET + colored.replace("§o", "");
    }

    public static void Bg(boolean value) {
        i = value;
    }

    public static boolean da() {
        return i;
    }

    public static boolean dc() {
        return !i;
    }

    private static RuntimeException a(RuntimeException runtimeException) {
        return runtimeException;
    }
}
