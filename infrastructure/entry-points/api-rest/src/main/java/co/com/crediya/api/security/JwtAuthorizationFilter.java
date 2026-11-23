package co.com.crediya.api.security;

import co.com.crediya.model.usuario.RolUsuario;
import co.com.crediya.model.usuario.TokenClaims;
import co.com.crediya.model.usuario.gateways.TokenProviderGateway;
import co.com.crediya.model.usuario.gateways.TokenRevocationGateway;
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
import java.time.LocalDateTime;

/**
 * Filtro de autorizacion para la validacion de tokens JWT y control de acceso RBAC.
 * Incluye verificacion activa contra la lista de revocacion (Blacklist).
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE + 10)
@WebFilter("/*")
public class JwtAuthorizationFilter implements Filter {

    public static final String CLAIMS_ATTRIBUTE = "TOKEN_CLAIMS";
    public static final String RAW_TOKEN_ATTRIBUTE = "RAW_BEARER_TOKEN";

    private final TokenProviderGateway tokenProviderGateway;
    private final TokenRevocationGateway tokenRevocationGateway;

    public JwtAuthorizationFilter(TokenProviderGateway tokenProviderGateway,
                                  TokenRevocationGateway tokenRevocationGateway) {
        this.tokenProviderGateway = tokenProviderGateway;
        this.tokenRevocationGateway = tokenRevocationGateway;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String path = httpRequest.getRequestURI();
        String method = httpRequest.getMethod();

        // 1. Rutas publicas y preflight CORS
        if ("OPTIONS".equalsIgnoreCase(method) ||
                path.startsWith("/api/v1/login") ||
                path.startsWith("/actuator") ||
                path.startsWith("/swagger-ui") ||
                path.startsWith("/v3/api-docs") ||
                path.startsWith("/webjars") ||
                path.equals("/swagger-ui.html")) {
            chain.doFilter(request, response);
            return;
        }

        // 2. Extraccion de cabecera de autorizacion
        String authHeader = httpRequest.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            escribirError(httpResponse, HttpServletResponse.SC_UNAUTHORIZED,
                    "No Autorizado", "Se requiere token de autenticacion Bearer valido");
            return;
        }

        String token = authHeader.substring(7).trim();

        // 3. Verificacion de revocacion inmediata (Blacklist)
        if (tokenRevocationGateway != null && tokenRevocationGateway.estaRevocado(token)) {
            escribirError(httpResponse, HttpServletResponse.SC_UNAUTHORIZED,
                    "No Autorizado", "El token de sesion ha sido revocado explicitamente");
            return;
        }

        TokenClaims claims;
        try {
            claims = tokenProviderGateway.validarToken(token);
        } catch (Exception e) {
            escribirError(httpResponse, HttpServletResponse.SC_UNAUTHORIZED,
                    "No Autorizado", "Token invalido o expirado: " + e.getMessage());
            return;
        }

        httpRequest.setAttribute(CLAIMS_ATTRIBUTE, claims);
        httpRequest.setAttribute(RAW_TOKEN_ATTRIBUTE, token);

        // 3. Reglas de control de acceso por endpoint (RBAC)
        if (path.startsWith("/api/v1/usuarios") && "POST".equalsIgnoreCase(method)) {
            if (claims.getRol() != RolUsuario.ADMIN && claims.getRol() != RolUsuario.ASESOR) {
                escribirError(httpResponse, HttpServletResponse.SC_FORBIDDEN,
                        "Acceso Prohibido", "Solo usuarios con rol Administrador o Asesor pueden registrar usuarios");
                return;
            }
        }

        if (path.startsWith("/api/v1/solicitud") && "POST".equalsIgnoreCase(method)) {
            if (claims.getRol() != RolUsuario.CLIENTE) {
                escribirError(httpResponse, HttpServletResponse.SC_FORBIDDEN,
                        "Acceso Prohibido", "Solo usuarios con rol Cliente pueden radicar solicitudes de credito");
                return;
            }
        }

        if (path.startsWith("/api/v1/solicitud") && "GET".equalsIgnoreCase(method)) {
            if (claims.getRol() != RolUsuario.ASESOR) {
                escribirError(httpResponse, HttpServletResponse.SC_FORBIDDEN,
                        "Acceso Prohibido", "Solo usuarios con rol Asesor pueden consultar el listado de solicitudes para revision");
                return;
            }
        }

        chain.doFilter(request, response);
    }

    private void escribirError(HttpServletResponse response, int status, String error, String mensaje) throws IOException {
        response.setStatus(status);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String cuerpo = "{\"timestamp\":\"" + LocalDateTime.now() + "\","
                + "\"status\":" + status + ","
                + "\"error\":\"" + escapeJson(error) + "\","
                + "\"mensaje\":\"" + escapeJson(mensaje) + "\"}";

        response.getWriter().write(cuerpo);
    }

    private String escapeJson(String texto) {
        if (texto == null) return "";
        return texto.replace("\\", "\\\\").replace("\"", "\\\"");
    }
}
