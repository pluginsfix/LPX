package pluginsfix.lpx.check.impl.position;
import pluginsfix.lpx.api.check.CheckResult;
import pluginsfix.lpx.api.check.FlagInfo;
import pluginsfix.lpx.api.check.options.CheckOptions;
import pluginsfix.lpx.api.player.PrinterStatus;
import pluginsfix.lpx.api.player.ProtocolPlayer;
import pluginsfix.lpx.check.base.AbstractCheckT;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerFlying;
import pluginsfix.lpx.player.ProtocolPlayerImpl;
import pluginsfix.lpx.util.Timer;
public class PositionCheckPrinter extends AbstractCheckT<WrapperPlayClientPlayerFlying> {
    private static final double y = 3.0E7;
    private static final double T = 2.0E7;
    public PositionCheckPrinter(ProtocolPlayer player, CheckOptions options) {
        super(player, options);
    }
    @Override
    protected CheckResult Bu(Object[] args) {
        ProtocolPlayer player = (ProtocolPlayer) args[1];
        long token = (Long) args[0];
        WrapperPlayClientPlayerFlying packet = (WrapperPlayClientPlayerFlying) args[2];
        return l0(new Object[]{player, token, packet});
    }
    public CheckResult l0(Object[] args) {
        ProtocolPlayer protocolPlayer = (ProtocolPlayer) args[0];
        long token = (Long) args[1];
        WrapperPlayClientPlayerFlying packet = (WrapperPlayClientPlayerFlying) args[2];
        if (!packet.hasPositionChanged()) {
            return CheckResult.ALLOW;
        }
        double x = packet.getLocation().getX();
        double yPos = packet.getLocation().getY();
        double z = packet.getLocation().getZ();
        if (!_x(new Object[]{x, y, token}) || !_x(new Object[]{yPos, T, token}) || !_x(new Object[]{z, y, token})) {
            AZ(new Object[]{
                packet,
                token,
                new FlagInfo[]{
                    FlagInfo.of("x", x),
                    FlagInfo.of("y", yPos),
                    FlagInfo.of("z", z)
                }
            });
            return CheckResult.DENY;
        }
        if (protocolPlayer.getPrinterData().getPrinterStatus() == PrinterStatus.AUTOMATIC
            && protocolPlayer instanceof ProtocolPlayerImpl protocolPlayerImpl
            && Timer.vS(new Object[]{protocolPlayer.getPrinterData().getLastPlace()})
            > protocolPlayerImpl.mk(new Object[0]).sZ(new Object[0]).Bw(new Object[0]).Y1(new Object[0]).xQ(new Object[0])) {
            protocolPlayer.getPrinterData().setPrinterMode(false);
        }
        return CheckResult.ALLOW;
    }
    @Override
    public boolean isPreVia() {
        return true;
    }
    private boolean _x(Object[] args) {
        double value = (Double) args[0];
        double maxAbs = (Double) args[1];
        if (Double.isNaN(value) || Double.isInfinite(value)) {
            return false;
        }
        return Math.abs(value) <= maxAbs;
    }
    private static RuntimeException a(RuntimeException runtimeException) {
        return runtimeException;
    }
}
