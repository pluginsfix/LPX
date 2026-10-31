package pluginsfix.lpx.api.check.enums;
public enum CheckType {
    NETTY_A(CheckCategory.NETTY, CheckID.A),
    NETTY_C(CheckCategory.NETTY, CheckID.C),
    WINDOW_A(CheckCategory.WINDOW, CheckID.A),
    WINDOW_B(CheckCategory.WINDOW, CheckID.B),
    WINDOW_C(CheckCategory.WINDOW, CheckID.C),
    WINDOW_D(CheckCategory.WINDOW, CheckID.D),
    ITEM_0(CheckCategory.ITEM, CheckID.ZERO),
    ITEM_1(CheckCategory.ITEM, CheckID.ONE),
    ITEM_2(CheckCategory.ITEM, CheckID.TWO),
    ITEM_3(CheckCategory.ITEM, CheckID.THREE),
    ITEM_A(CheckCategory.ITEM, CheckID.A),
    ITEM_B(CheckCategory.ITEM, CheckID.B),
    ITEM_C(CheckCategory.ITEM, CheckID.C),
    ITEM_D(CheckCategory.ITEM, CheckID.D),
    ITEM_E(CheckCategory.ITEM, CheckID.E),
    ITEM_F(CheckCategory.ITEM, CheckID.F),
    ITEM_G(CheckCategory.ITEM, CheckID.G),
    ITEM_H(CheckCategory.ITEM, CheckID.H),
    ITEM_I(CheckCategory.ITEM, CheckID.I),
    ITEM_J(CheckCategory.ITEM, CheckID.J),
    CREATIVE_A(CheckCategory.CREATIVE, CheckID.A),
    CREATIVE_B(CheckCategory.CREATIVE, CheckID.B),
    CREATIVE_C(CheckCategory.CREATIVE, CheckID.C),
    CREATIVE_D(CheckCategory.CREATIVE, CheckID.D),
    CREATIVE_E(CheckCategory.CREATIVE, CheckID.E),
    CREATIVE_F(CheckCategory.CREATIVE, CheckID.F),
    CREATIVE_G(CheckCategory.CREATIVE, CheckID.G),
    PLACE_A(CheckCategory.PLACE, CheckID.A),
    PLACE_B(CheckCategory.PLACE, CheckID.B),
    PLACE_C(CheckCategory.PLACE, CheckID.C),
    FLOOD_A(CheckCategory.FLOOD, CheckID.A),
    FLOOD_B(CheckCategory.FLOOD, CheckID.B),
    FLOOD_C(CheckCategory.FLOOD, CheckID.C),
    FLOOD_D(CheckCategory.FLOOD, CheckID.D),
    FLOOD_F(CheckCategory.FLOOD, CheckID.F),
    FLOOD_G(CheckCategory.FLOOD, CheckID.G),
    POSITION_A(CheckCategory.POSITION, CheckID.A),
    POSITION_B(CheckCategory.POSITION, CheckID.B),
    POSITION_C(CheckCategory.POSITION, CheckID.C),
    POSITION_D(CheckCategory.POSITION, CheckID.D),
    POSITION_E(CheckCategory.POSITION, CheckID.E),
    PAYLOAD_A(CheckCategory.PAYLOAD, CheckID.A),
    PAYLOAD_B(CheckCategory.PAYLOAD, CheckID.B),
    PAYLOAD_C(CheckCategory.PAYLOAD, CheckID.C),
    PAYLOAD_D(CheckCategory.PAYLOAD, CheckID.D),
    PAYLOAD_G(CheckCategory.PAYLOAD, CheckID.G),
    PAYLOAD_E(CheckCategory.PAYLOAD, CheckID.E),
    PAYLOAD_F(CheckCategory.PAYLOAD, CheckID.F),
    BOOK_A(CheckCategory.BOOK, CheckID.A),
    BOOK_B(CheckCategory.BOOK, CheckID.B),
    BOOK_C(CheckCategory.BOOK, CheckID.C),
    BOOK_0(CheckCategory.BOOK, CheckID.ZERO),
    TAB_A(CheckCategory.TAB, CheckID.A),
    TAB_B(CheckCategory.TAB, CheckID.B),
    COMMAND_A(CheckCategory.COMMAND, CheckID.A),
    RECIPE_A(CheckCategory.RECIPE, CheckID.A),
    RECIPE_B(CheckCategory.RECIPE, CheckID.B);
    private final CheckCategory category;
    private final CheckID id;
    private final String description;
    private final int priority;
    private final boolean beta;
    private static String legacyState;
    CheckType(CheckCategory category, CheckID id) {
        this(category, id, category.getFormattedName() + " " + id.name(), 0, false);
    }
    CheckType(CheckCategory category, CheckID id, String description) {
        this(category, id, description, 0, false);
    }
    CheckType(CheckCategory category, CheckID id, String description, int priority) {
        this(category, id, description, priority, false);
    }
    CheckType(CheckCategory category, CheckID id, String description, boolean beta) {
        this(category, id, description, 0, beta);
    }
    CheckType(CheckCategory category, CheckID id, String description, int priority, boolean beta) {
        this.category = category;
        this.id = id;
        this.description = description;
        this.priority = priority;
        this.beta = beta;
    }
    public CheckCategory getCategory() {
        return category;
    }
    public CheckID getId() {
        return id;
    }
    public String getDescription() {
        return description;
    }
    public int getPriority() {
        return priority;
    }
    public boolean isBeta() {
        return beta;
    }
    public static void RE(String value) {
        legacyState = value;
    }
    public static String Wg() {
        return legacyState;
    }
}
