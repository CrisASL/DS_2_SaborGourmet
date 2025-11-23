package cl.ipss.eva2.controllers;

import cl.ipss.eva2.models.Mesa;
import cl.ipss.eva2.services.MesaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/mesas")
public class MesaController {

    private final MesaService mesaService;

    public MesaController(MesaService mesaService) {
        this.mesaService = mesaService;
    }

    // LISTAR TODAS LAS MESAS
    @GetMapping
    public String listarMesas(Model model) {
        model.addAttribute("mesas", mesaService.getAll());
        return "mesas/lista-mesas";
    }

    // LISTAR SOLO MESAS DISPONIBLES
    @GetMapping("/disponibles")
    public String listarMesasDisponibles(Model model) {
        model.addAttribute("mesas", mesaService.getDisponibles());
        return "mesas/lista-mesas";
    }

    // FORMULARIO CREAR MESA
    @GetMapping("/crear")
    public String mostrarFormularioCrear(Model model) {
        model.addAttribute("mesa", new Mesa());
        model.addAttribute("accion", "crear");
        return "mesas/form-mesa";
    }

    // GUARDAR NUEVA MESA
    @PostMapping("/guardar")
    public String guardarMesa(@ModelAttribute Mesa mesa, Model model) {
        // VALIDACIONES BÁSICAS
        if (mesa.getNumero() <= 0 || mesa.getCapacidad() <= 0) {
            model.addAttribute("error", "Número y capacidad deben ser mayores a 0");
            model.addAttribute("accion", "crear");
            return "mesas/form-mesa";
        }

        mesa.setDisponible(true); // por defecto, nueva mesa disponible
        mesaService.create(mesa);
        return "redirect:/mesas";
    }

    // FORMULARIO EDITAR MESA
    @GetMapping("/editar/{id}")
    public String editarMesa(@PathVariable Long id, Model model) {
        Mesa mesa = mesaService.getById(id);
        if (mesa == null) {
            return "redirect:/mesas";
        }
        model.addAttribute("mesa", mesa);
        model.addAttribute("accion", "editar");
        return "mesas/form-mesa";
    }

    // ACTUALIZAR MESA
    @PostMapping("/editar/{id}")
    public String actualizarMesa(@PathVariable Long id, @ModelAttribute Mesa mesa, Model model) {
        if (mesa.getNumero() <= 0 || mesa.getCapacidad() <= 0) {
            model.addAttribute("error", "Número y capacidad deben ser mayores a 0");
            model.addAttribute("accion", "editar");
            return "mesas/form-mesa";
        }

        mesaService.update(id, mesa);
        return "redirect:/mesas";
    }

    // ELIMINAR MESA
    @GetMapping("/eliminar/{id}")
    public String eliminarMesa(@PathVariable Long id) {
        try {
            mesaService.delete(id);
        } catch (RuntimeException e) {
            // Manejar error si la mesa no existe
        }
        return "redirect:/mesas";
    }

    // CAMBIAR DISPONIBILIDAD
    @GetMapping("/disponible/{id}")
    public String cambiarDisponibilidad(@PathVariable Long id) {
        try {
            mesaService.cambiarDisponibilidad(id);
        } catch (RuntimeException e) {
            // Manejar error si la mesa no existe
        }
        return "redirect:/mesas";
    }
}

