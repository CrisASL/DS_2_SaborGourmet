package cl.ipss.eva2.exceptions;

public class ClienteDuplicadoException extends RuntimeException {
    public ClienteDuplicadoException(String mensaje) {
        super(mensaje);
    }
}
