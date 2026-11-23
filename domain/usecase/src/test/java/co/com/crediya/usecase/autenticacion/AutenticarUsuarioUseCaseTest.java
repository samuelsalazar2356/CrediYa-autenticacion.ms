package co.com.crediya.usecase.autenticacion;

import co.com.crediya.model.usuario.RolUsuario;
import co.com.crediya.model.usuario.SesionUsuario;
import co.com.crediya.model.usuario.Usuario;
import co.com.crediya.model.usuario.excepcion.BusinessException;
import co.com.crediya.model.usuario.gateways.PasswordEncoderGateway;
import co.com.crediya.model.usuario.gateways.TokenProviderGateway;
import co.com.crediya.model.usuario.gateways.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AutenticarUsuarioUseCaseTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PasswordEncoderGateway passwordEncoderGateway;

    @Mock
    private TokenProviderGateway tokenProviderGateway;

    private AutenticarUsuarioUseCase autenticarUsuarioUseCase;

    private Usuario usuarioValido;

    @BeforeEach
    void setUp() {
        autenticarUsuarioUseCase = new AutenticarUsuarioUseCase(
                usuarioRepository, passwordEncoderGateway, tokenProviderGateway);

        usuarioValido = Usuario.builder()
                .id(1L)
                .cedula("1098765432")
                .nombres("Carlos")
                .apellidos("Perez")
                .correoElectronico("carlos@crediya.com")
                .contrasenaHash("hashSeguro123")
                .rol(RolUsuario.CLIENTE)
                .estado("ACTIVO")
                .salarioBase(new BigDecimal("3500000"))
                .fechaNacimiento(LocalDate.of(1995, 5, 15))
                .build();
    }

    @Test
    void shouldAuthenticateSuccessfully() {
        when(usuarioRepository.buscarPorCorreoElectronico("carlos@crediya.com"))
                .thenReturn(Optional.of(usuarioValido));
        when(passwordEncoderGateway.verificar("Clave123*", "hashSeguro123"))
                .thenReturn(true);
        when(tokenProviderGateway.generarToken(any(Usuario.class)))
                .thenReturn("token.jwt.simulado");

        SesionUsuario sesion = autenticarUsuarioUseCase.autenticar("carlos@crediya.com", "Clave123*");

        assertNotNull(sesion);
        assertEquals("token.jwt.simulado", sesion.getToken());
        assertEquals("Bearer", sesion.getTipoToken());
        assertEquals(1L, sesion.getUsuario().getId());
        verify(tokenProviderGateway).generarToken(usuarioValido);
    }

    @Test
    void shouldThrowExceptionWhenUserNotFound() {
        when(usuarioRepository.buscarPorCorreoElectronico("noexiste@crediya.com"))
                .thenReturn(Optional.empty());

        BusinessException ex = assertThrows(BusinessException.class, () ->
                autenticarUsuarioUseCase.autenticar("noexiste@crediya.com", "Clave123*"));

        assertEquals("Credenciales de autenticacion invalidas", ex.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenPasswordDoesNotMatch() {
        when(usuarioRepository.buscarPorCorreoElectronico("carlos@crediya.com"))
                .thenReturn(Optional.of(usuarioValido));
        when(passwordEncoderGateway.verificar("ClaveErronea*", "hashSeguro123"))
                .thenReturn(false);

        BusinessException ex = assertThrows(BusinessException.class, () ->
                autenticarUsuarioUseCase.autenticar("carlos@crediya.com", "ClaveErronea*"));

        assertEquals("Credenciales de autenticacion invalidas", ex.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenUserIsInactive() {
        Usuario usuarioInactivo = usuarioValido.toBuilder().estado("INACTIVO").build();

        when(usuarioRepository.buscarPorCorreoElectronico("carlos@crediya.com"))
                .thenReturn(Optional.of(usuarioInactivo));
        when(passwordEncoderGateway.verificar("Clave123*", "hashSeguro123"))
                .thenReturn(true);

        BusinessException ex = assertThrows(BusinessException.class, () ->
                autenticarUsuarioUseCase.autenticar("carlos@crediya.com", "Clave123*"));

        assertEquals("El usuario se encuentra inactivo en el sistema", ex.getMessage());
    }
}
