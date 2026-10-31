package pluginsfix.lpx.check.impl.book;
import pluginsfix.lpx.api.check.CheckResult;
import pluginsfix.lpx.api.check.FlagInfo;
import pluginsfix.lpx.api.check.options.CheckOptions;
import pluginsfix.lpx.api.player.ProtocolPlayer;
import pluginsfix.lpx.check.base.AbstractCheckT;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientEditBook;
import java.util.List;
public class BookCheckPageCount extends AbstractCheckT<WrapperPlayClientEditBook> {
    private int maxPages;
    public BookCheckPageCount(ProtocolPlayer player, CheckOptions options) {
        super(player, options);
    }
    @Override
    public void loadOptions() {
        maxPages = j_(new Object[0]).getInt(kD(new Object[]{"max", 0L}), 100);
    }
    @Override
    protected CheckResult Bu(Object[] args) {
        return Q0(args);
    }
    protected CheckResult Q0(Object[] args) {
        WrapperPlayClientEditBook packet = (WrapperPlayClientEditBook) args[2];
        if (packet == null) {
            return CheckResult.ALLOW;
        }
        List<String> pages = packet.getPages();
        int pagesCount = pages == null ? 0 : pages.size();
        if (pagesCount <= maxPages) {
            return CheckResult.ALLOW;
        }
        AZ(
            packet,
            0L,
            new FlagInfo[]{
                FlagInfo.of("pages", pagesCount),
                FlagInfo.of("max-pages", maxPages)
            }
        );
        return CheckResult.DENY;
    }
    private static RuntimeException a(RuntimeException runtimeException) {
        return runtimeException;
    }
}
