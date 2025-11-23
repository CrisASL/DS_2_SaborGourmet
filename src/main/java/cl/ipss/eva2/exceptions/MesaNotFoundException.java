package cl.ipss.eva2.exceptions;

public class MesaNotFoundException extends RuntimeException {

    public MesaNotFoundException() {
        super();
    }

    public MesaNotFoundException(String message) {
        super(message);
    }

    public MesaNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public MesaNotFoundException(Throwable cause) {
        super(cause);
    }
}
