package br.com.bjbraz.audit.exchange;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;

import javax.crypto.spec.PBEKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.*;
import java.util.Base64;

/**
 * This is the Main class.
 */
class SimpleTests {


    public static void main(String[] args) throws UnsupportedEncodingException {

        String password = "user";
        String salt = "mysecret";
        int iterations = 33;
        int keyLength = 256;
        char[] passwordchars = password.toCharArray();
        byte[] saltBytes = salt.getBytes();

        byte[] hashedBytes = hashPassword(passwordchars, saltBytes, iterations, keyLength);
        //String hashedString = Hex.encodeHexString(hashedBytes);

        System.out.println(Base64.getEncoder().encodeToString(hashedBytes));
    }

    public static byte[] hashPassword( final char[] password, final byte[] salt, final int iterations, final int keyLength ) {

        try {
            SecretKeyFactory skf = SecretKeyFactory.getInstance( "PBKDF2WithHmacSHA512" );
            PBEKeySpec spec = new PBEKeySpec( password, salt, iterations, keyLength );
            SecretKey key = skf.generateSecret( spec );
            byte[] res = key.getEncoded( );
            return res;
        } catch ( Exception e ) {
            throw new RuntimeException ( "e" );
        }
    }
}