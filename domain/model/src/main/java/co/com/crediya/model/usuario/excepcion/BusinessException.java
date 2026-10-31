package co.com.crediya.model.usuario.excepcion;

public class BusinessException extends RuntimeException {
    public BusinessException(String message) {
        super(message);
    }
}