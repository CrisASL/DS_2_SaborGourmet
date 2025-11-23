package cl.ipss.eva2.exceptions;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MesaNotFoundException.class)
    public String manejarMesaNoEncontrada(MesaNotFoundException ex, Model model) {
        model.addAttribute("error", ex.getMessage());
        return "error/error-general";
    }

    @ExceptionHandler(MesaOcupadaException.class)
    public String manejarMesaOcupada(MesaOcupadaException ex, Model model) {
        model.addAttribute("error", ex.getMessage());
        return "error/error-general";
    }

    @ExceptionHandler(DuplicadoException.class)
    public String manejarDuplicado(DuplicadoException ex, Model model) {
        model.addAttribute("error", ex.getMessage());
        return "error/error-general";
    }

    @ExceptionHandler(RecursoNoEncontradoException.class)
    public String manejarNoEncontrado(RecursoNoEncontradoException ex, Model model) {
        model.addAttribute("error", ex.getMessage());
        return "error/error-general";
    }

    @ExceptionHandler(ClienteDuplicadoException.class)
    public String manejarClienteDuplicado(ClienteDuplicadoException ex, Model model) {
        model.addAttribute("error", ex.getMessage());
        return "error/error-general";
    }

    @ExceptionHandler(ClienteNotFoundException.class)
    public String manejarClienteNoEncontrado(ClienteNotFoundException ex, Model model) {
        model.addAttribute("error", ex.getMessage());
        return "error/error-general";
    }

    @ExceptionHandler(ValidacionException.class)
    public String manejarValidacion(ValidacionException ex, Model model) {
        model.addAttribute("error", ex.getMessage());
        return "error/error-general";
    }

    // fallback para cualquier otra excepción
    @ExceptionHandler(Exception.class)
    public String manejarGenerico(Exception ex, Model model) {
        model.addAttribute("error", "Ocurrió un error inesperado: " + ex.getMessage());
        return "error/error-general";
    }
}



