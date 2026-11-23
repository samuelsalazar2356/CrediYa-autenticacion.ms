package co.com.crediya.api.security;

import co.com.crediya.model.usuario.gateways.TokenRevocationGateway;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Adaptador de infraestructura en memoria para la lista de revocacion de tokens (Blacklist).
 * En un entorno distribuido multi-nodo, este adaptador se sustituye por una implementacion respaldada en Redis.
 */
@Component
public class InMemoryTokenRevocationAdapter implements TokenRevocationGateway {

    private final Map<String, LocalDateTime> tokensRevocados = new ConcurrentHashMap<>();

    @Override
    public void revocarToken(String token, LocalDateTime expiracion) {
        if (token == null || token.trim().isEmpty()) {
            return;
        }
        tokensRevocados.put(token.trim(), expiracion != null ? expiracion : LocalDateTime.now().plusHours(1));
        limpiarTokensExpirados();
    }

    @Override
    public boolean estaRevocado(String token) {
        if (token == null || token.trim().isEmpty()) {
            return false;
        }
        LocalDateTime expiracion = tokensRevocados.get(token.trim());
        if (expiracion == null) {
            return false;
        }
        if (LocalDateTime.now().isAfter(expiracion)) {
            tokensRevocados.remove(token.trim());
            return false;
        }
        return true;
    }

    private void limpiarTokensExpirados() {
        if (tokensRevocados.size() > 500) {
            LocalDateTime ahora = LocalDateTime.now();
            tokensRevocados.entrySet().removeIf(entry -> ahora.isAfter(entry.getValue()));
        }
    }
}
