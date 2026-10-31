package pluginsfix.lpx.util;
public class Timer {
    private static int g;
    private Timer() {
        Timer a2;
    }
    public static long vS(Object[] a2) {
        long a3 = (Long)a2[0];
        return Timer.Wz(new Object[0]) - a3;
    }
    public static long Wz(Object[] objectArray) {
        return System.currentTimeMillis();
    }
    public static void fo(int n) {
        g = n;
    }
    public static int WJ() {
        return g;
    }
    public static int W1() {
        int n = Timer.WJ();
        try {
            if (n == 0) {
                return 123;
            }
        }
        catch (RuntimeException runtimeException) {
            throw Timer.a(runtimeException);
        }
        return 0;
    }
    private static RuntimeException a(RuntimeException runtimeException) {
        return runtimeException;
    }
    static {
        if (Timer.W1() == 0) {
            Timer.fo(25);
        }
    }
}
