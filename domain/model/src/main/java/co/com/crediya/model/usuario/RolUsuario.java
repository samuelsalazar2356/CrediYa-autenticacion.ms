package co.com.crediya.model.usuario;

import co.com.crediya.model.usuario.excepcion.BusinessException;

import java.util.Arrays;

/**
 * Roles de autorizacion del sistema CrediYa para control de acceso RBAC.
 */
public enum RolUsuario {
    ADMIN("Administrador"),
    ASESOR("Asesor de Credito"),
    CLIENTE("Cliente Solicitante");

    private final String descripcion;

    RolUsuario(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public static RolUsuario fromString(String valor) {
        if (valor == null || valor.trim().isEmpty()) {
            return CLIENTE;
        }

        String normalizado = valor.trim().toUpperCase();
        for (RolUsuario rol : RolUsuario.values()) {
            if (rol.name().equals(normalizado)) {
                return rol;
            }
        }

        throw new BusinessException("El rol '" + valor + "' no es valido. Roles permitidos: " + Arrays.toString(RolUsuario.values()));
    }
}
