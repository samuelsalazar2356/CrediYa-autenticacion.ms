package co.com.crediya.api.security;

import co.com.crediya.model.usuario.RolUsuario;
import co.com.crediya.model.usuario.TokenClaims;
import co.com.crediya.model.usuario.Usuario;
import co.com.crediya.model.usuario.excepcion.BusinessException;
import co.com.crediya.model.usuario.gateways.TokenProviderGateway;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Base64;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Proveedor de tokens JWT basado estrictamente en el estandar RFC 7519.
 * Emplea HMAC-SHA256 y serializacion nativa sin dependencias externas.
 */
@Component
public class JwtTokenProviderAdapter implements TokenProviderGateway {

    private static final String HMAC_SHA256 = "HmacSHA256";
    private static final String JWT_HEADER_BASE64 = Base64.getUrlEncoder().withoutPadding()
            .encodeToString("{\"alg\":\"HS256\",\"typ\":\"JWT\"}".getBytes(StandardCharsets.UTF_8));

    private static final Pattern PATTERN_SUB = Pattern.compile("\"sub\"\\s*:\\s*\"([^\"]+)\"");
    private static final Pattern PATTERN_ID = Pattern.compile("\"id\"\\s*:\\s*(\\d+)");
    private static final Pattern PATTERN_CEDULA = Pattern.compile("\"cedula\"\\s*:\\s*\"([^\"]+)\"");
    private static final Pattern PATTERN_ROL = Pattern.compile("\"rol\"\\s*:\\s*\"([^\"]+)\"");
    private static final Pattern PATTERN_EXP = Pattern.compile("\"exp\"\\s*:\\s*(\\d+)");

    private final String secretKey;
    private final long expirationMinutes;

    public JwtTokenProviderAdapter(
            @Value("${security.jwt.secret:CrediYaSuperSecretKeyForJwtAuthentication2026ProductionStandardMustBe256BitsLong!}") String secretKey,
            @Value("${security.jwt.expiration-minutes:60}") long expirationMinutes) {
        this.secretKey = secretKey;
        this.expirationMinutes = expirationMinutes;
    }

    @Override
    public String generarToken(Usuario usuario) {
        try {
            long nowSeconds = Instant.now().getEpochSecond();
            long expSeconds = nowSeconds + (expirationMinutes * 60);

            String rolStr = usuario.getRol() != null ? usuario.getRol().name() : RolUsuario.CLIENTE.name();
            Long id = usuario.getId() != null ? usuario.getId() : 0L;
            String cedula = usuario.getCedula() != null ? escapeJson(usuario.getCedula()) : "";
            String sub = usuario.getCorreoElectronico() != null ? escapeJson(usuario.getCorreoElectronico()) : "";

            String payloadJson = "{\"sub\":\"" + sub + "\","
                    + "\"id\":" + id + ","
                    + "\"cedula\":\"" + cedula + "\","
                    + "\"rol\":\"" + rolStr + "\","
                    + "\"iat\":" + nowSeconds + ","
                    + "\"exp\":" + expSeconds + "}";

            String payloadBase64 = Base64.getUrlEncoder().withoutPadding()
                    .encodeToString(payloadJson.getBytes(StandardCharsets.UTF_8));

            String dataToSign = JWT_HEADER_BASE64 + "." + payloadBase64;
            String signature = sign(dataToSign);

            return dataToSign + "." + signature;
        } catch (Exception e) {
            throw new BusinessException("Error al generar token de autenticacion: " + e.getMessage());
        }
    }

    @Override
    public TokenClaims validarToken(String token) {
        if (token == null || token.trim().isEmpty()) {
            throw new BusinessException("El token de autenticacion no puede ser nulo ni vacio");
        }

        String tokenLimpio = token.startsWith("Bearer ") ? token.substring(7).trim() : token.trim();
        String[] partes = tokenLimpio.split("\\.");
        if (partes.length != 3) {
            throw new BusinessException("Estructura de token JWT invalida");
        }

        String dataToSign = partes[0] + "." + partes[1];
        String expectedSignature = sign(dataToSign);

        if (!MessageDigest.isEqual(
                expectedSignature.getBytes(StandardCharsets.UTF_8),
                partes[2].getBytes(StandardCharsets.UTF_8))) {
            throw new BusinessException("Firma de token JWT invalida");
        }

        try {
            byte[] payloadBytes = Base64.getUrlDecoder().decode(partes[1]);
            String payloadJson = new String(payloadBytes, StandardCharsets.UTF_8);

            long expSeconds = extraerLong(PATTERN_EXP, payloadJson, 0L);
            long nowSeconds = Instant.now().getEpochSecond();

            if (expSeconds < nowSeconds) {
                throw new BusinessException("El token de sesion ha expirado");
            }

            Long id = extraerLong(PATTERN_ID, payloadJson, null);
            String cedula = extraerString(PATTERN_CEDULA, payloadJson);
            String sub = extraerString(PATTERN_SUB, payloadJson);
            String rolStr = extraerString(PATTERN_ROL, payloadJson);
            RolUsuario rol = RolUsuario.fromString(rolStr != null ? rolStr : RolUsuario.CLIENTE.name());

            LocalDateTime expDateTime = LocalDateTime.ofInstant(Instant.ofEpochSecond(expSeconds), ZoneOffset.UTC);

            return new TokenClaims(id, cedula, sub, rol, expDateTime);
        } catch (BusinessException be) {
            throw be;
        } catch (Exception e) {
            throw new BusinessException("Error al procesar el payload del token JWT: " + e.getMessage());
        }
    }

    private String sign(String data) {
        try {
            Mac mac = Mac.getInstance(HMAC_SHA256);
            SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), HMAC_SHA256);
            mac.init(secretKeySpec);
            byte[] signedBytes = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
            return Base64.getUrlEncoder().withoutPadding().encodeToString(signedBytes);
        } catch (Exception e) {
            throw new IllegalStateException("Fallo al firmar el token criptografico", e);
        }
    }

    private String escapeJson(String valor) {
        return valor.replace("\\", "\\\\").replace("\"", "\\\"");
    }

    private String extraerString(Pattern patron, String json) {
        Matcher matcher = patron.matcher(json);
        return matcher.find() ? matcher.group(1) : null;
    }

    private Long extraerLong(Pattern patron, String json, Long valorDefecto) {
        Matcher matcher = patron.matcher(json);
        if (matcher.find()) {
            return Long.parseLong(matcher.group(1));
        }
        return valorDefecto;
    }
}
