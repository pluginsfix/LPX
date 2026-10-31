package pluginsfix.lpx.api.check.options;
public class PersonalCheckOptions {
    private final CheckOptions checkOptions;
    private int vl;
    private double buffer;
    public PersonalCheckOptions(CheckOptions checkOptions) {
        this.checkOptions = checkOptions;
    }
    public CheckOptions getCheckOptions() {
        return checkOptions;
    }
    public int getVl() {
        return vl;
    }
    public void setVl(int vl) {
        this.vl = vl;
    }
    public void increaseVl() {
        vl++;
    }
    public double getBuffer() {
        return buffer;
    }
    public void setBuffer(double buffer) {
        this.buffer = buffer;
    }
    public void reset() {
        vl = 0;
        buffer = 0.0D;
    }
}
