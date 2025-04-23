package au.com.tabcorp.toolkit.api.utils;

import org.apache.commons.codec.binary.Base64;

import java.security.KeyFactory;
import java.security.Security;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class RSAKeyUtils {

    private static final String ALGORIGTHM_RSA = "RSA";

    static {
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
    }

    public static RSAPrivateKey readPKCS8PrivateKey(final String privateKey) throws Exception {
        final KeyFactory factory = KeyFactory.getInstance(ALGORIGTHM_RSA, new org.bouncycastle.jce.provider.BouncyCastleProvider());
        return (RSAPrivateKey) factory.generatePrivate(new PKCS8EncodedKeySpec(Base64.decodeBase64(privateKey)));
    }

    public static RSAPublicKey readX509PublicKey(final String publicKey) throws Exception {
      final KeyFactory factory = KeyFactory.getInstance(ALGORIGTHM_RSA, new org.bouncycastle.jce.provider.BouncyCastleProvider());
      return (RSAPublicKey) factory.generatePublic(new X509EncodedKeySpec(Base64.decodeBase64( publicKey )));
    }

}
