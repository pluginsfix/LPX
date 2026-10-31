package pluginsfix.lpx.util;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
public class AESCipher {
    private final byte[] keyBytes;
    private final SecretKeySpec keySpec;
    private final Cipher cipher;
    public AESCipher(String keySeed, int ignoredA, char ignoredB, short ignoredC)
        throws NoSuchPaddingException, NoSuchAlgorithmException {
        this(0L, keySeed.getBytes());
    }
    public AESCipher(long ignored, byte[] seed) throws NoSuchPaddingException, NoSuchAlgorithmException {
        this.keyBytes = ByteUtils.Jj(new Object[]{seed, 0L});
        this.keySpec = new SecretKeySpec(this.keyBytes, "AES");
        this.cipher = Cipher.getInstance("AES");
    }
    public byte[] GL(Object[] args) throws IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
        String data = (String) args[0];
        return Jg(new Object[]{data.getBytes()});
    }
    public synchronized byte[] Jg(Object[] args) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        byte[] data = (byte[]) args[0];
        cipher.init(Cipher.ENCRYPT_MODE, keySpec);
        return cipher.doFinal(data);
    }
    public synchronized byte[] JF(Object[] args) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        byte[] data = (byte[]) args[0];
        cipher.init(Cipher.DECRYPT_MODE, keySpec);
        return cipher.doFinal(data);
    }
}
