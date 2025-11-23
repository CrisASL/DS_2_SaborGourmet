package cl.ipss.eva2.controllers;

import cl.ipss.eva2.exceptions.DuplicadoException;
import cl.ipss.eva2.exceptions.MesaOcupadaException;
import cl.ipss.eva2.exceptions.RecursoNoEncontradoException;
import cl.ipss.eva2.exceptions.ValidacionException;
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

        try {
            mesaService.create(mesa);
            return "redirect:/mesas";

        } catch (ValidacionException | DuplicadoException e) {
            model.addAttribute("mesa", mesa);
            model.addAttribute("accion", "crear");
            model.addAttribute("error", e.getMessage());
            return "mesas/form-mesa";
        }
    }

    // FORMULARIO EDITAR MESA
    @GetMapping("/editar/{id}")
    public String editarMesa(@PathVariable Long id, Model model) {

        try {
            Mesa mesa = mesaService.getById(id);

            model.addAttribute("mesa", mesa);
            model.addAttribute("accion", "editar");
            return "mesas/form-mesa";

        } catch (RecursoNoEncontradoException e) {
            model.addAttribute("error", e.getMessage());
            return "mesas/lista-mesas";
        }
    }

    // ACTUALIZAR MESA
    @PostMapping("/editar/{id}")
    public String actualizarMesa(@PathVariable Long id, @ModelAttribute Mesa mesa, Model model) {

        try {
            mesaService.update(id, mesa);
            return "redirect:/mesas";

        } catch (ValidacionException | RecursoNoEncontradoException | DuplicadoException e) {
            model.addAttribute("mesa", mesa);
            model.addAttribute("accion", "editar");
            model.addAttribute("error", e.getMessage());
            return "mesas/form-mesa";
        }
    }

    // ELIMINAR MESA
    @GetMapping("/eliminar/{id}")
    public String eliminarMesa(@PathVariable Long id, Model model) {

        try {
            mesaService.delete(id);
            return "redirect:/mesas";

        } catch (MesaOcupadaException | RecursoNoEncontradoException e) {
            model.addAttribute("error", e.getMessage());
            return "mesas/lista-mesas";
        }
    }

    // CAMBIAR DISPONIBILIDAD
    @GetMapping("/disponible/{id}")
    public String cambiarDisponibilidad(@PathVariable Long id, Model model) {

        try {
            mesaService.cambiarDisponibilidad(id);
            return "redirect:/mesas";

        } catch (RecursoNoEncontradoException e) {
            model.addAttribute("error", e.getMessage());
            return "mesas/lista-mesas";
        }
    }
}


