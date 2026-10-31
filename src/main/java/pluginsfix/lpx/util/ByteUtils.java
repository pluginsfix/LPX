package pluginsfix.lpx.util;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.IOException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
public final class ByteUtils {
    private static final String DEFAULT_HASH = "SHA-256";
    private static int[] w;
    private ByteUtils() {
    }
    public static String Jg(Object[] args) {
        byte[] bytes = (byte[]) args[0];
        return new String(bytes);
    }
    public static byte[] Jj(Object[] args) {
        byte[] bytes = (byte[]) args[0];
        try {
            MessageDigest digest = MessageDigest.getInstance(DEFAULT_HASH);
            return digest.digest(bytes);
        } catch (NoSuchAlgorithmException exception) {
            exception.printStackTrace();
            return null;
        }
    }
    public static byte[] Jo(Object[] args) {
        byte[] first = (byte[]) args[1];
        byte[] second = (byte[]) args[2];
        byte[] merged = new byte[first.length + second.length];
        System.arraycopy(first, 0, merged, 0, first.length);
        System.arraycopy(second, 0, merged, first.length, second.length);
        return merged;
    }
    public static byte[] J2(Object[] args) throws NoSuchAlgorithmException {
        byte[] bytes = (byte[]) args[1];
        MessageDigest digest = MessageDigest.getInstance(DEFAULT_HASH);
        digest.update(bytes);
        return digest.digest();
    }
    public static byte[] YZ(Object[] args) throws IOException, NoSuchAlgorithmException {
        InputStream input = (InputStream) args[0];
        byte[] raw = Yj(new Object[]{input, 0L});
        return J2(new Object[]{0L, raw});
    }
    public static byte[] Kb(Object[] args) throws IOException, NoSuchAlgorithmException {
        URL url = (URL) args[0];
        try (InputStream input = url.openStream()) {
            return YZ(new Object[]{input, 0L});
        }
    }
    public static byte[] Yj(Object[] args) throws IOException {
        InputStream input = (InputStream) args[0];
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        byte[] buffer = new byte[8192];
        int read;
        while ((read = input.read(buffer, 0, buffer.length)) != -1) {
            output.write(buffer, 0, read);
        }
        output.flush();
        return output.toByteArray();
    }
    public static void T4(int[] values) {
        w = values;
    }
    public static int[] fA() {
        return w;
    }
    private static Exception a(Exception exception) {
        return exception;
    }
}
