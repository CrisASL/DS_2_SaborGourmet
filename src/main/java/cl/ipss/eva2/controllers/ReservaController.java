package cl.ipss.eva2.controllers;

import cl.ipss.eva2.models.Reserva;
import cl.ipss.eva2.services.ReservaService;
import cl.ipss.eva2.services.ClienteService;
import cl.ipss.eva2.services.MesaService;
import cl.ipss.eva2.exceptions.ValidacionException;
import cl.ipss.eva2.models.Mesa;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

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
            model.addAttribute("mesas", mesaService.getAll());

            // Generar lista de horas en medias horas
            List<String> horas = new ArrayList<>();
            for (int h = 10; h <= 21; h++) {
                horas.add(String.format("%02d:00", h));
                horas.add(String.format("%02d:30", h));
            }
            model.addAttribute("horas", horas);

            model.addAttribute("accion", "crear");
            return "reservas/form-reserva";
        }


    // GUARDAR RESERVA
    @PostMapping("/guardar")
    public String guardarReserva(@ModelAttribute Reserva reserva, Model model) {
        try {
            reservaService.create(reserva);
            return "redirect:/reservas";

        } catch (ValidacionException e) {
            model.addAttribute("reserva", reserva);
            model.addAttribute("clientes", clienteService.getAll());
            model.addAttribute("mesas", mesaService.getDisponiblesPara(reserva.getFecha(), reserva.getHora()));
            model.addAttribute("accion", "crear");
            model.addAttribute("error", e.getMessage());
            return "reservas/form-reserva";
        }
    }

    // FORMULARIO EDITAR RESERVA
    @GetMapping("/editar/{id}")
    public String editarReserva(@PathVariable Long id, Model model) {
        try {
            Reserva reserva = reservaService.getById(id);
            model.addAttribute("reserva", reserva);
            model.addAttribute("clientes", clienteService.getAll());
            model.addAttribute("mesas", mesaService.getDisponiblesPara(reserva.getFecha(), reserva.getHora()));
            model.addAttribute("accion", "editar");
            return "reservas/form-reserva";

        } catch (ValidacionException e) {
            model.addAttribute("error", e.getMessage());
            return "error/error-general";
        }
    }

    // ACTUALIZAR RESERVA
    @PostMapping("/editar/{id}")
    public String actualizarReserva(@PathVariable Long id, @ModelAttribute Reserva reserva, Model model) {
        try {
            reservaService.update(id, reserva);
            return "redirect:/reservas";

        } catch (ValidacionException e) {
            model.addAttribute("reserva", reserva);
            model.addAttribute("clientes", clienteService.getAll());
            model.addAttribute("mesas", mesaService.getDisponiblesPara(reserva.getFecha(), reserva.getHora()));
            model.addAttribute("accion", "editar");
            model.addAttribute("error", e.getMessage());
            return "reservas/form-reserva";
        }
    }

    // CANCELAR RESERVA
    @GetMapping("/cancelar/{id}")
    public String cancelarReserva(@PathVariable Long id, Model model) {
        try {
            reservaService.delete(id);
            return "redirect:/reservas";

        } catch (ValidacionException e) {
            model.addAttribute("error", e.getMessage());
            return "error/error-general";
        }
    }
}




