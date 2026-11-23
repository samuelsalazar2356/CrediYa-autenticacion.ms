package co.com.crediya.api.security;

import co.com.crediya.model.usuario.gateways.PasswordEncoderGateway;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

/**
 * Adaptador de cifrado de contrasenas basado en el estandar NIST SP 800-63B.
 * Utiliza PBKDF2WithHmacSHA256 con salt criptografico aleatorio e iteraciones defensivas.
 */
@Component
public class PBKDF2PasswordEncoderAdapter implements PasswordEncoderGateway {

    private static final int ITERATIONS = 100000;
    private static final int KEY_LENGTH = 256;
    private static final int SALT_BYTES = 16;
    private static final String ALGORITHM = "PBKDF2WithHmacSHA256";
    private final SecureRandom secureRandom = new SecureRandom();

    @Override
    public String codificar(String contrasenaPlana) {
        if (contrasenaPlana == null || contrasenaPlana.isEmpty()) {
            throw new IllegalArgumentException("La contrasena plana no puede ser nula ni vacia");
        }

        byte[] salt = new byte[SALT_BYTES];
        secureRandom.nextBytes(salt);

        byte[] hash = pbkdf2(contrasenaPlana.toCharArray(), salt, ITERATIONS, KEY_LENGTH);

        String saltBase64 = Base64.getEncoder().encodeToString(salt);
        String hashBase64 = Base64.getEncoder().encodeToString(hash);

        return ITERATIONS + ":" + saltBase64 + ":" + hashBase64;
    }

    @Override
    public boolean verificar(String contrasenaPlana, String contrasenaCodificada) {
        if (contrasenaPlana == null || contrasenaCodificada == null) {
            return false;
        }

        String[] partes = contrasenaCodificada.split(":");
        if (partes.length != 3) {
            // Manejo de compatibilidad en caso de contrasena no formateada
            return contrasenaPlana.equals(contrasenaCodificada);
        }

        try {
            int iteraciones = Integer.parseInt(partes[0]);
            byte[] salt = Base64.getDecoder().decode(partes[1]);
            byte[] hashEsperado = Base64.getDecoder().decode(partes[2]);

            byte[] hashCalculado = pbkdf2(contrasenaPlana.toCharArray(), salt, iteraciones, hashEsperado.length * 8);

            return MessageDigest.isEqual(hashEsperado, hashCalculado);
        } catch (Exception e) {
            return false;
        }
    }

    private byte[] pbkdf2(char[] password, byte[] salt, int iterations, int keyLength) {
        try {
            PBEKeySpec spec = new PBEKeySpec(password, salt, iterations, keyLength);
            SecretKeyFactory skf = SecretKeyFactory.getInstance(ALGORITHM);
            return skf.generateSecret(spec).getEncoded();
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new IllegalStateException("Error al ejecutar hashing criptografico de contrasena", e);
        }
    }
}
