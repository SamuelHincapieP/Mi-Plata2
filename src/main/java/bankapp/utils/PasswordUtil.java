package bankapp.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;


public class PasswordUtil {

    private static final int SALT_BYTES = 16;

    private PasswordUtil() {}

    /**
     * Hashea una contraseña con salt aleatorio.
     * Retorna el string "SALT_BASE64:HASH_BASE64" listo para guardar en BD.
     */
    public static String hash(String plainPassword) {
        try {
            byte[] salt = new byte[SALT_BYTES];
            new SecureRandom().nextBytes(salt);
            byte[] hash = sha256(plainPassword, salt);
            return Base64.getEncoder().encodeToString(salt)
                    + ":" + Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error al hashear contraseña", e);
        }
    }

    /**
     * Verifica que una contraseña en texto plano coincida con el hash almacenado.
     * También acepta contraseñas legacy (texto plano sin prefijo SALT:HASH)
     * para no romper cuentas existentes.
     */
    public static boolean verify(String plainPassword, String storedValue) {
        if (storedValue == null || plainPassword == null) return false;

        // Contraseña legacy (texto plano): comparación directa
        if (!storedValue.contains(":")) {
            return storedValue.equals(plainPassword);
        }

        // Contraseña hasheada: extraer salt y recalcular
        try {
            String[] parts = storedValue.split(":", 2);
            byte[] salt = Base64.getDecoder().decode(parts[0]);
            byte[] expectedHash = Base64.getDecoder().decode(parts[1]);
            byte[] actualHash   = sha256(plainPassword, salt);
            return MessageDigest.isEqual(expectedHash, actualHash); // comparación segura contra timing attacks
        } catch (Exception e) {
            return false;
        }
    }

    private static byte[] sha256(String password, byte[] salt) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(salt);
        return md.digest(password.getBytes());
    }
}
