package base.security;

import java.util.Base64;

public class Base64Test {

    public static void main(String[] args) {
        String message = "You still have lots more to work on";
        System.out.println("original："+message);
        byte[] bytes = message.getBytes();
        byte[] encode = encode(bytes);
        decode(encode);
    }

    private static byte[] encode(byte[] bytesToEncode) {
        Base64.Encoder encoder = Base64.getUrlEncoder();
        byte[] encoded = encoder.encode(bytesToEncode);
        System.out.println("Encoded: " + new String(encoded));
        return encoded;
    }

    private static void decode(byte[] encoded) {
        Base64.Decoder decoder = Base64.getUrlDecoder();
        byte[] decoded = decoder.decode(encoded);
        System.out.println("Decoded: " + new String(decoded));
    }
}
