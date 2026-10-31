package pluginsfix.lpx.check.impl.book;
import pluginsfix.lpx.api.check.CheckResult;
import pluginsfix.lpx.api.check.FlagInfo;
import pluginsfix.lpx.api.check.options.CheckOptions;
import pluginsfix.lpx.api.player.ProtocolPlayer;
import pluginsfix.lpx.check.base.AbstractCheckT;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientEditBook;
import java.util.List;
public class BookCheckNBT extends AbstractCheckT<WrapperPlayClientEditBook> {
    private int maxPages;
    private int maxPageLength;
    private int maxTitleLength;
    public BookCheckNBT(ProtocolPlayer player, CheckOptions options) {
        super(player, options);
    }
    @Override
    public void loadOptions() {
        maxPages = j_(new Object[0]).getInt(kD(new Object[]{"max-pages", 0L}), 100);
        maxPageLength = j_(new Object[0]).getInt(kD(new Object[]{"max-page-length", 0L}), 8192);
        maxTitleLength = j_(new Object[0]).getInt(kD(new Object[]{"max-title-length", 0L}), 128);
    }
    @Override
    protected CheckResult Bu(Object[] args) {
        return h7(args);
    }
    public CheckResult h7(Object[] args) {
        WrapperPlayClientEditBook packet = (WrapperPlayClientEditBook) args[2];
        if (packet == null) {
            return CheckResult.ALLOW;
        }
        List<String> pages = packet.getPages();
        if (pages != null) {
            if (pages.size() > maxPages) {
                AZ(
                    packet,
                    0L,
                    new FlagInfo[]{
                        FlagInfo.of("pages", pages.size()),
                        FlagInfo.of("max-pages", maxPages)
                    }
                );
                return CheckResult.DENY;
            }
            for (String page : pages) {
                int len = page == null ? 0 : page.length();
                if (len > maxPageLength) {
                    AZ(
                        packet,
                        0L,
                        new FlagInfo[]{
                            FlagInfo.of("page-length", len),
                            FlagInfo.of("max-page-length", maxPageLength)
                        }
                    );
                    return CheckResult.DENY;
                }
            }
        }
        String title = packet.getTitle();
        if (title != null && title.length() > maxTitleLength) {
            AZ(
                packet,
                0L,
                new FlagInfo[]{
                    FlagInfo.of("title-length", title.length()),
                    FlagInfo.of("max-title-length", maxTitleLength)
                }
            );
            return CheckResult.DENY;
        }
        return CheckResult.ALLOW;
    }
    @Override
    public boolean isPreVia() {
        return true;
    }
    private static RuntimeException a(RuntimeException runtimeException) {
        return runtimeException;
    }
}
