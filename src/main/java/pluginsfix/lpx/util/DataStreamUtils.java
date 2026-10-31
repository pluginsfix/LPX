package pluginsfix.lpx.util;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;
public class DataStreamUtils {
    private DataStreamUtils() {
        DataStreamUtils a2;
    }
    public static void ri(Object[] a2) throws IOException {
        OutputStream a3 = (OutputStream)a2[0];
        byte[] a4 = (byte[])a2[1];
        DataOutputStream a5 = new DataOutputStream(a3);
        a5.writeInt(a4.length);
        a5.write(a4);
        a5.flush();
    }
    public static byte[] Ec(Object[] a2) throws IOException {
        InputStream a3 = (InputStream)a2[0];
        DataInputStream a4 = new DataInputStream(a3);
        int a5 = a4.readInt();
        byte[] a6 = new byte[a5];
        a4.readFully(a6);
        return a6;
    }
}
