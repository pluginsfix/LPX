package pluginsfix.lpx.stats;
import pluginsfix.lpx.config.MessagesConfig;
import pluginsfix.lpx.core.LPXCore;
import pluginsfix.lpx.network.MetricSample;
import pluginsfix.lpx.network.NetworkStatsCollector;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.bukkit.ChatColor;
public class StatsFormatter {
    private final DecimalFormat numberFormat;
    private final String primaryColor;
    private final String secondaryColor;
    public StatsFormatter(long ignored, LPXCore core) {
        this.numberFormat = new DecimalFormat("0.0000");
        MessagesConfig messagesConfig = core.sZ(new Object[0]).BM(new Object[0]);
        this.primaryColor = messagesConfig.Vj(new Object[0]);
        this.secondaryColor = messagesConfig.Vp(new Object[0]);
    }
    public String wm(Object[] args) {
        NetworkStatsCollector collector = (NetworkStatsCollector) args[1];
        StringBuilder builder = new StringBuilder();
        builder.append(primaryColor).append("Network Stats ").append(secondaryColor)
            .append("(avg/median/p95/max)\n");
        XA(new Object[]{
            builder,
            "Main",
            collector.nP(new Object[]{0L}),
            0L,
            collector.nD(new Object[]{0L}),
            collector.nC(new Object[]{0L}),
            collector.nI(new Object[]{0L})
        });
        XA(new Object[]{
            builder,
            "Median",
            collector.nv(new Object[]{0L}),
            0L,
            collector.ns(new Object[]{0L}),
            collector.nK(new Object[]{0L}),
            collector.nd(new Object[]{0L})
        });
        XA(new Object[]{
            builder,
            "P95",
            collector.nH(new Object[]{0L}),
            0L,
            collector.nU(new Object[]{0L}),
            collector.nf(new Object[]{0L}),
            collector.n_(new Object[]{0L})
        });
        BigDecimal p90 = collector.ni(new Object[]{0L});
        builder.append('\n')
            .append(primaryColor).append("Overall P90: ")
            .append(cr(new Object[]{p90.doubleValue(), 0L}))
            .append(ChatColor.GRAY).append(" ").append(ch(new Object[]{p90.doubleValue()}))
            .append('\n');
        @SuppressWarnings("unchecked")
        Map<Integer, MetricSample> byThread = collector.nS(new Object[]{0L});
        if (!byThread.isEmpty()) {
            builder.append(primaryColor).append("By Thread:\n");
            List<Integer> indexes = new ArrayList<>(byThread.keySet());
            Collections.sort(indexes);
            for (Integer index : indexes) {
                MetricSample sample = byThread.get(index);
                double value = sample.bL(new Object[0]).doubleValue();
                int players = sample.bu(new Object[0]);
                builder.append(ChatColor.GRAY)
                    .append("[")
                    .append(index + 1)
                    .append("] ")
                    .append(cr(new Object[]{value, 0L}))
                    .append(" (")
                    .append(players)
                    .append(players == 1 ? " player" : " players")
                    .append(")");
                if (ca(new Object[]{0L, value})) {
                    builder.append(" ").append(ch(new Object[]{value}));
                }
                builder.append('\n');
            }
        }
        return builder.toString();
    }
    private void XA(Object[] args) {
        StringBuilder builder = (StringBuilder) args[0];
        String label = (String) args[1];
        BigDecimal average = (BigDecimal) args[2];
        BigDecimal median = (BigDecimal) args[4];
        BigDecimal p95 = (BigDecimal) args[5];
        BigDecimal max = (BigDecimal) args[6];
        builder.append(secondaryColor)
            .append(" - ")
            .append(label)
            .append(" ")
            .append(cI(new Object[]{average.doubleValue(), 0L}))
            .append(ch(new Object[]{average.doubleValue()}))
            .append(ChatColor.GRAY).append("/")
            .append(cI(new Object[]{median.doubleValue(), 0L}))
            .append(ch(new Object[]{median.doubleValue()}))
            .append(ChatColor.GRAY).append("/")
            .append(cI(new Object[]{p95.doubleValue(), 0L}))
            .append(ch(new Object[]{p95.doubleValue()}))
            .append(ChatColor.GRAY).append("/")
            .append(cI(new Object[]{max.doubleValue(), 0L}))
            .append(ch(new Object[]{max.doubleValue()}))
            .append('\n');
    }
    private String cI(Object[] args) {
        double value = (Double) args[0];
        return cr(new Object[]{value, 0L});
    }
    private String cr(Object[] args) {
        double value = (Double) args[0];
        return numberFormat.format(value);
    }
    private String ch(Object[] args) {
        double value = (Double) args[0];
        if (value >= 50.0D) {
            return ChatColor.RED + " HIGH";
        }
        if (value >= 20.0D) {
            return ChatColor.GOLD + " WARN";
        }
        return ChatColor.GREEN + " OK";
    }
    private boolean ca(Object[] args) {
        double value = (Double) args[1];
        return value >= 20.0D;
    }
}
