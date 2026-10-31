package pluginsfix.lpx.printer;
import pluginsfix.lpx.alert.AlertManager;
import pluginsfix.lpx.api.player.PrinterData;
import pluginsfix.lpx.api.player.PrinterStatus;
import pluginsfix.lpx.api.player.ProtocolPlayer;
import pluginsfix.lpx.config.PrinterConfig;
import pluginsfix.lpx.core.LPXCore;
import pluginsfix.lpx.util.Timer;
public class PrinterDataImpl implements PrinterData {
    private static final long COUNTER_RESET_MS = 1000L;
    private final LPXCore core;
    private final ProtocolPlayer player;
    private PrinterStatus status;
    private long lastPlaceAt;
    private long counterWindowStartAt;
    private int blocksPlaced;
    public PrinterDataImpl(LPXCore core, ProtocolPlayer player, long ignored) {
        this.core = core;
        this.player = player;
        this.status = PrinterStatus.NONE;
        this.lastPlaceAt = Timer.Wz(new Object[0]);
        this.counterWindowStartAt = Timer.Wz(new Object[0]);
        this.blocksPlaced = 0;
    }
    @Override
    public PrinterStatus getPrinterStatus() {
        return status;
    }
    @Override
    public boolean isUsingPrinter() {
        return status != PrinterStatus.NONE;
    }
    @Override
    public void setPrinterMode(boolean enabled) {
        setPrinterMode(enabled ? PrinterStatus.API : PrinterStatus.NONE);
    }
    @Override
    public void setPrinterMode(PrinterStatus mode) {
        this.status = mode;
        PrinterConfig printerConfig = core.sZ(new Object[0]).Bw(new Object[0]).Y1(new Object[0]);
        if (!printerConfig.xE(new Object[0])) {
            return;
        }
        String permission = printerConfig.xm(new Object[0]);
        AlertManager alertManager = core.s1(new Object[0]);
        if (mode == PrinterStatus.NONE) {
            String disabledMessage = alertManager.e5(new Object[]{0L, printerConfig.xO(new Object[0]), player});
            alertManager.v_(new Object[]{disabledMessage, permission});
            return;
        }
        String enabledMessage = alertManager.e5(new Object[]{0L, printerConfig.xB(new Object[0]), player})
            .replace("%printer-mode%", mode.name());
        alertManager.v_(new Object[]{enabledMessage, permission});
    }
    @Override
    public void updateLastPlace() {
        this.lastPlaceAt = Timer.Wz(new Object[0]);
    }
    @Override
    public long getLastPlace() {
        return lastPlaceAt;
    }
    @Override
    public int increaseBlocksPlaced() {
        if (Timer.vS(new Object[]{counterWindowStartAt}) > COUNTER_RESET_MS) {
            counterWindowStartAt = Timer.Wz(new Object[0]);
            blocksPlaced = 0;
        }
        blocksPlaced++;
        return blocksPlaced;
    }
    @Override
    public int getBlocksPlaced() {
        return blocksPlaced;
    }
}
