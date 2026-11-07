package co.com.crediya.api.dto;

import co.com.crediya.model.usuario.Usuario;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.util.Locale;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuarioResponseDTO {
    private String mensaje;
    private Usuario usuario;
    private String salarioFormateado;
    private LocalDateTime timestamp;

    public static UsuarioResponseDTO exitoso(Usuario usuario) {
        String salarioFormateado = null;
        if (usuario.getSalarioBase() != null) {
            NumberFormat formatoCOP = NumberFormat.getCurrencyInstance(new Locale("es", "CO"));
            formatoCOP.setMaximumFractionDigits(2);
            salarioFormateado = formatoCOP.format(usuario.getSalarioBase());
        }

        return UsuarioResponseDTO.builder()
                .mensaje("Usuario registrado exitosamente en el sistema CrediYa")
                .usuario(usuario)
                .salarioFormateado(salarioFormateado)
                .timestamp(LocalDateTime.now())
                .build();
    }
}
