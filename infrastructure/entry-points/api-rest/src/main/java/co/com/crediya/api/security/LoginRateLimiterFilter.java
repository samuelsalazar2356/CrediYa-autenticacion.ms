package co.com.crediya.api.security;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Filtro de mitigacion contra ataques de denegacion de servicio por fuerza bruta (CPU Starvation).
 * Protege el costo computacional de PBKDF2 (100k iteraciones) mediante limitacion de tasa por IP cliente,
 * respetando el criterio de negocio de intentos ilimitados a nivel de cuenta (sin bloqueo de usuario).
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE + 5)
@WebFilter("/api/v1/login")
public class LoginRateLimiterFilter implements Filter {

    private static final int MAX_SOLICITUDES_POR_VENTANA = 10;
    private static final long VENTANA_SEGUNDOS = 10;

    private final Map<String, VentanaRateLimit> intentosPorIp = new ConcurrentHashMap<>();

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String path = httpRequest.getRequestURI();
        String method = httpRequest.getMethod();

        if ("POST".equalsIgnoreCase(method) && path.endsWith("/api/v1/login")) {
            String ipCliente = obtenerIpCliente(httpRequest);
            if (!permitirPeticion(ipCliente)) {
                httpResponse.setStatus(429);
                httpResponse.setHeader("Retry-After", String.valueOf(VENTANA_SEGUNDOS));
                httpResponse.setContentType("application/json");
                httpResponse.setCharacterEncoding("UTF-8");

                String cuerpo = "{\"timestamp\":\"" + LocalDateTime.now() + "\","
                        + "\"status\":429,"
                        + "\"error\":\"Demasiadas Peticiones\","
                        + "\"mensaje\":\"Tasa limite de autenticacion excedida desde esta direccion IP. Reintente en " + VENTANA_SEGUNDOS + " segundos.\"}";

                httpResponse.getWriter().write(cuerpo);
                return;
            }
        }

        chain.doFilter(request, response);
    }

    private boolean permitirPeticion(String ipCliente) {
        long ahora = Instant.now().getEpochSecond();
        limpiarSiEsNecesario(ahora);

        VentanaRateLimit ventana = intentosPorIp.compute(ipCliente, (ip, actual) -> {
            if (actual == null || (ahora - actual.inicioVentanaEpoch) > VENTANA_SEGUNDOS) {
                return new VentanaRateLimit(ahora, new AtomicInteger(1));
            }
            actual.contador.incrementAndGet();
            return actual;
        });

        return ventana.contador.get() <= MAX_SOLICITUDES_POR_VENTANA;
    }

    private String obtenerIpCliente(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.trim().isEmpty()) {
            return xForwardedFor.split(",")[0].trim();
        }
        return request.getRemoteAddr() != null ? request.getRemoteAddr() : "0.0.0.0";
    }

    private void limpiarSiEsNecesario(long ahora) {
        if (intentosPorIp.size() > 1000) {
            intentosPorIp.entrySet().removeIf(entry -> (ahora - entry.getValue().inicioVentanaEpoch) > VENTANA_SEGUNDOS * 2);
        }
    }

    private static class VentanaRateLimit {
        final long inicioVentanaEpoch;
        final AtomicInteger contador;

        VentanaRateLimit(long inicioVentanaEpoch, AtomicInteger contador) {
            this.inicioVentanaEpoch = inicioVentanaEpoch;
            this.contador = contador;
        }
    }
}
