package urbanutility.design.kaleidoscope.clients;

import org.apache.commons.codec.binary.Hex;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by jerye on 1/4/2018.
 * https://github.com/binance-exchange/binance-java-api/blob/master/src/main/java/com/binance/api/client/security/HmacSHA256Signer.java
 */

public class HmacSigner{
    private static String ALGORITHM_HMAC_SHA256 = "HmacSHA256";
    private static String ALGORITHM_HMAC_SHA512 = "HmacSHA512";
    private static String ALGORITHM_HMAC_MD5 = "HmacMD5";




    //Binance = query string + key
    public static String signSHA256(String message, String secretKey){
        try{
            Mac hmac_sha256 = Mac.getInstance(ALGORITHM_HMAC_SHA256);
            SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(), ALGORITHM_HMAC_SHA256);
            hmac_sha256.init(secretKeySpec);
            byte[] bytes = hmac_sha256.doFinal(message.getBytes());
            return new String(Hex.encodeHex(bytes));

        }catch(NoSuchAlgorithmException | InvalidKeyException e){
            throw new RuntimeException("Failed to sign message", e);
        }
    }

    //Cryptopia = nonce + time?
    public static String signMD5(){
        return null;
    }

    //Bittrex = nonce + key
    public static String signSHA512(String key){
        return null;
    }


}
