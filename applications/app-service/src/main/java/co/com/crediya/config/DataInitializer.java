package co.com.crediya.config;

import co.com.crediya.model.usuario.RolUsuario;
import co.com.crediya.model.usuario.Usuario;
import co.com.crediya.model.usuario.gateways.PasswordEncoderGateway;
import co.com.crediya.model.usuario.gateways.UsuarioRepository;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Seeds the database with minimal admin, asesor and cliente accounts if they do not exist.
 * Only runs on application start in the authentication microservice.
 */
@Component
public class DataInitializer implements ApplicationListener<ApplicationReadyEvent> {
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoderGateway passwordEncoder;

    public DataInitializer(UsuarioRepository usuarioRepository, PasswordEncoderGateway passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        // Admin user
        ensureUser("admin@crediya.com", "admin", "ADMIN", "AdminPass123*");
        // Asesor user
        ensureUser("asesor@crediya.com", "asesor", "ASESOR", "AsesorPass123*");
        // Cliente user
        ensureUser("cliente@crediya.com", "cliente", "CLIENTE", "ClientePass123*");
    }

    private void ensureUser(String email, String cedula, String rolStr, String plainPassword) {
        Optional<Usuario> existing = usuarioRepository.buscarPorCorreoElectronico(email);
        if (existing.isPresent()) {
            return;
        }
        String hashed = passwordEncoder.codificar(plainPassword);
        Usuario user = Usuario.builder()
                .correoElectronico(email)
                .cedula(cedula)
                .nombres("Seed " + rolStr)
                .apellidos("User")
                .fechaNacimiento(java.time.LocalDate.of(1995, 1, 1))
                .telefono("3001234567")
                .salarioBase(new java.math.BigDecimal("5000000"))
                .contrasenaHash(hashed)
                .rol(RolUsuario.valueOf(rolStr))
                .estado("ACTIVO")
                .fechaCreacion(LocalDateTime.now())
                .build();
        usuarioRepository.guardar(user);
    }
}
