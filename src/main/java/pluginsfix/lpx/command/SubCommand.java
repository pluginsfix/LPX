package pluginsfix.lpx.command;
import pluginsfix.lpx.core.LPXCore;
import org.bukkit.command.CommandSender;
public abstract class SubCommand {
    protected final LPXCore core;
    private final String name;
    private final String permission;
    private final String description;
    private final boolean playerOnly;
    private final String usage;
    private static int legacyState;
    protected SubCommand(LPXCore core, String name, String permission, String description, boolean playerOnly) {
        this(core, name, permission, description, playerOnly, "");
    }
    protected SubCommand(LPXCore core, String name, String permission, String description, boolean playerOnly, String usage) {
        this.core = core;
        this.name = name;
        this.permission = permission;
        this.description = description;
        this.playerOnly = playerOnly;
        this.usage = usage;
    }
    protected abstract void executeCommand(CommandSender sender, String[] args);
    public final void execute(CommandSender sender, String[] args) {
        executeCommand(sender, args);
    }
    public final void eK(Object[] args) {
        CommandSender sender = (CommandSender) args[0];
        String[] commandArgs = (String[]) args[1];
        executeCommand(sender, commandArgs);
    }
    public LPXCore getCore() {
        return core;
    }
    public String getName() {
        return name;
    }
    public String getPermission() {
        return permission;
    }
    public String getDescription() {
        return description;
    }
    public boolean isPlayerOnly() {
        return playerOnly;
    }
    public String getUsage() {
        return usage;
    }
    public String w4(Object[] ignored) {
        return getName();
    }
    public String wm(Object[] ignored) {
        return getPermission();
    }
    public String wE(Object[] ignored) {
        return getDescription();
    }
    public boolean wj(Object[] ignored) {
        return isPlayerOnly();
    }
    public String wT(Object[] ignored) {
        return getUsage();
    }
    protected void sendPrefixedMessage(CommandSender sender, String message) {
        sender.sendMessage(core.sZ(new Object[0]).BM(new Object[0]).V8(new Object[0]) + " " + message);
    }
    public void TQ(Object[] args) {
        CommandSender sender = (CommandSender) args[0];
        String message = (String) args[1];
        sendPrefixedMessage(sender, message);
    }
    public static void ef(int value) {
        legacyState = value;
    }
    public static int wo() {
        return legacyState;
    }
    public static int wZ() {
        return legacyState == 0 ? 61 : 0;
    }
    static {
        if (legacyState != 0) {
            legacyState = 65;
        }
    }
}
