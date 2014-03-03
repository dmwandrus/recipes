package mil.navair.iframework.common.utilities;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

import com.sun.mail.util.BASE64DecoderStream;
import com.sun.mail.util.BASE64EncoderStream;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EncriptAndDecript {

    private static Cipher encipher;
    private static Cipher decipher;
    private static final int iterationCount = 20;
    private static String passPhrase = "AkEPODhWiMeAoEMdIFjR23i0948hdEwMCiDeN2MD!aDda";
    // 8-byte Salt
    private static byte[] salt = {
        (byte) 0xB2, (byte) 0x12, (byte) 0xD5, (byte) 0xB2,
        (byte) 0x44, (byte) 0x21, (byte) 0xC3, (byte) 0xC3
    };
    private static EncriptAndDecript encriptAndDecript = new EncriptAndDecript();

    private EncriptAndDecript() {
        try {
            // create a user-chosen password that can be used with password-based encryption (PBE)
            // provide password, salt, iteration count for generating PBEKey of fixed-key-size PBE ciphers
            KeySpec keySpec = new PBEKeySpec(passPhrase.toCharArray(), salt, iterationCount);

            // create a secret (symmetric) key using PBE with MD5 and DES
            SecretKey key = SecretKeyFactory.getInstance("PBEWithMD5AndDES").generateSecret(keySpec);

            // construct a parameter set for password-based encryption as defined in the PKCS #5 standard
            AlgorithmParameterSpec paramSpec = new PBEParameterSpec(salt, iterationCount);

            encipher = Cipher.getInstance(key.getAlgorithm());
            decipher = Cipher.getInstance(key.getAlgorithm());

            // initialize the ciphers with the given key
            encipher.init(Cipher.ENCRYPT_MODE, key, paramSpec);
            decipher.init(Cipher.DECRYPT_MODE, key, paramSpec);
        } catch (InvalidAlgorithmParameterException e) {
            System.out.println("Invalid Alogorithm Parameter:" + e.getMessage());

        } catch (InvalidKeySpecException e) {
            System.out.println("Invalid Key Spec:" + e.getMessage());

        } catch (NoSuchAlgorithmException e) {
            System.out.println("No Such Algorithm:" + e.getMessage());

        } catch (NoSuchPaddingException e) {
            System.out.println("No Such Padding:" + e.getMessage());

        } catch (InvalidKeyException e) {
            System.out.println("Invalid Key:" + e.getMessage());

        }
    }

    public static String encrypt(String str) {
        String password = null;
        
        if (str != null) {
            try {
                // encode the string into a sequence of bytes using the named charset
                // storing the result into a new byte array.
                byte[] utf8 = str.getBytes("UTF8");
                byte[] enc = encipher.doFinal(utf8);

                // encode to base64
                enc = BASE64EncoderStream.encode(enc);
                password = new String(enc);
            } catch (Exception e) {
                Logger.getLogger(EncriptAndDecript.class.getName()).log(Level.INFO, null, e);
            }
        }

        return password;
    }

    public static String decrypt(String str) {
        String password = null;

        if (str != null) {
            try {
                // decode with base64 to get bytes
                byte[] dec = BASE64DecoderStream.decode(str.getBytes());

                byte[] utf8 = decipher.doFinal(dec);

                // create new string based on the specified charset
                password = new String(utf8, "UTF8");
            } catch (Exception e) {
                Logger.getLogger(EncriptAndDecript.class.getName()).log(Level.INFO, null, e);
            }
        }

        return password;
    }

    public static void main(String args[]) {
        String password = "ManFromUncle";

        String encryptedPassword = EncriptAndDecript.encrypt(password);
        Logger.getLogger(EncriptAndDecript.class.getName()).log(Level.INFO, "The encrypted password: {0}", encryptedPassword);
        String decryptedPassword = EncriptAndDecript.decrypt(encryptedPassword);
        Logger.getLogger(EncriptAndDecript.class.getName()).log(Level.INFO, "The decrypted password: {0}", decryptedPassword);
    }
}
