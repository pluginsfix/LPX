package pluginsfix.lpx.check.impl.nbt;
class NBTParseResult {
    private final boolean valid;
    private final int maxDepthSeen;
    private final int maxCountSeen;
    private final NBTLimitReason reason;
    private NBTParseResult(boolean valid, int maxDepthSeen, int maxCountSeen, NBTLimitReason reason) {
        this.valid = valid;
        this.maxDepthSeen = maxDepthSeen;
        this.maxCountSeen = maxCountSeen;
        this.reason = reason;
    }
    public static NBTParseResult success(int maxDepthSeen, int maxCountSeen) {
        return new NBTParseResult(true, maxDepthSeen, maxCountSeen, null);
    }
    public static NBTParseResult depthExceeded(int maxDepthSeen, int maxCountSeen) {
        return new NBTParseResult(false, maxDepthSeen, maxCountSeen, NBTLimitReason.MAX_DEPTH_EXCEEDED);
    }
    public static NBTParseResult countExceeded(int maxDepthSeen, int maxCountSeen) {
        return new NBTParseResult(false, maxDepthSeen, maxCountSeen, NBTLimitReason.MAX_COUNT_EXCEEDED);
    }
    public static NBTParseResult QW(Object[] args) {
        int depth = (Integer) args[0];
        int count = (Integer) args[1];
        return success(depth, count);
    }
    public static NBTParseResult Q7(Object[] args) {
        int depth = (Integer) args[0];
        int count = (Integer) args[1];
        return depthExceeded(depth, count);
    }
    public static NBTParseResult QN(Object[] args) {
        int depth = (Integer) args[0];
        int count = (Integer) args[1];
        return countExceeded(depth, count);
    }
    public boolean isValid() {
        return valid;
    }
    public int getMaxDepthSeen() {
        return maxDepthSeen;
    }
    public int getMaxCountSeen() {
        return maxCountSeen;
    }
    public NBTLimitReason getReason() {
        return reason;
    }
    public boolean gg(Object[] args) {
        return isValid();
    }
    public int gO(Object[] args) {
        return getMaxDepthSeen();
    }
    public int gm(Object[] args) {
        return getMaxCountSeen();
    }
    public NBTLimitReason g9(Object[] args) {
        return getReason();
    }
}
