package cl.ipss.eva2.controllers;

import cl.ipss.eva2.models.Reserva;
import cl.ipss.eva2.services.ReservaService;
import cl.ipss.eva2.services.ClienteService;
import cl.ipss.eva2.services.MesaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/reservas")
public class ReservaController {

    private final ReservaService reservaService;
    private final ClienteService clienteService;
    private final MesaService mesaService;

    public ReservaController(ReservaService reservaService, ClienteService clienteService, MesaService mesaService) {
        this.reservaService = reservaService;
        this.clienteService = clienteService;
        this.mesaService = mesaService;
    }

    // LISTAR RESERVAS
    @GetMapping
    public String listarReservas(Model model) {
        model.addAttribute("reservas", reservaService.getAll());
        return "reservas/lista-reservas";
    }

    // FORMULARIO NUEVA RESERVA
    @GetMapping("/crear")
    public String mostrarFormularioReserva(Model model) {
        model.addAttribute("reserva", new Reserva());
        model.addAttribute("clientes", clienteService.getAll());
        model.addAttribute("mesas", mesaService.getAll()); // O getDisponibles si agregas ese método
        return "reservas/form-reserva";
    }

    // GUARDAR RESERVA
    @PostMapping("/guardar")
    public String guardarReserva(@ModelAttribute Reserva reserva) {
        reservaService.create(reserva);
        return "redirect:/reservas";
    }

    // EDITAR RESERVA
    @GetMapping("/editar/{id}")
    public String editarReserva(@PathVariable Long id, Model model) {
        model.addAttribute("reserva", reservaService.getById(id));
        model.addAttribute("clientes", clienteService.getAll());
        model.addAttribute("mesas", mesaService.getAll());
        return "reservas/form-reserva";
    }

    // ACTUALIZAR RESERVA
    @PostMapping("/editar/{id}")
    public String actualizarReserva(@PathVariable Long id, @ModelAttribute Reserva reserva) {
        reservaService.update(id, reserva);
        return "redirect:/reservas";
    }

    // CANCELAR RESERVA
    @GetMapping("/cancelar/{id}")
    public String cancelarReserva(@PathVariable Long id) {
        // En versión A, cancelamos eliminando la reserva
        reservaService.delete(id);
        return "redirect:/reservas";
    }
}

