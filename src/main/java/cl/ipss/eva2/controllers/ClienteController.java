package cl.ipss.eva2.controllers;

import cl.ipss.eva2.exceptions.ClienteNotFoundException;
import cl.ipss.eva2.exceptions.ValidacionException;
import cl.ipss.eva2.models.Cliente;
import cl.ipss.eva2.services.ClienteService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/clientes")
public class ClienteController {

    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    // LISTAR CLIENTES
    @GetMapping
    public String listarClientes(Model model) {
        model.addAttribute("clientes", clienteService.getAll());
        return "clientes/lista-clientes";
    }

    // FORMULARIO CREAR
    @GetMapping("/crear")
    public String mostrarFormularioCrear(Model model) {
        model.addAttribute("cliente", new Cliente());
        model.addAttribute("accion", "crear");
        return "clientes/form-cliente";
    }

    // GUARDAR CLIENTE NUEVO
    @PostMapping("/guardar")
    public String guardarCliente(@ModelAttribute Cliente cliente, Model model) {
        try {
            clienteService.create(cliente);
            return "redirect:/clientes";

        } catch (ValidacionException e) {
            // Vuelve al formulario con el mensaje de validación
            model.addAttribute("cliente", cliente);
            model.addAttribute("accion", "crear");
            model.addAttribute("error", e.getMessage());
            return "clientes/form-cliente";
        }
    }

    // FORMULARIO EDITAR
    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Long id, Model model) {
        try {
            Cliente cliente = clienteService.getById(id);

            model.addAttribute("cliente", cliente);
            model.addAttribute("accion", "editar");
            return "clientes/form-cliente";

        } catch (ClienteNotFoundException e) {
            // Redirige a la lista, mostrando mensaje global
            model.addAttribute("error", e.getMessage());
            return "clientes/lista-clientes";
        }
    }

    // ACTUALIZAR CLIENTE EXISTENTE
    @PostMapping("/editar/{id}")
    public String actualizarCliente(@PathVariable Long id, @ModelAttribute Cliente cliente, Model model) {
        try {
            clienteService.update(id, cliente);
            return "redirect:/clientes";

        } catch (ClienteNotFoundException | ValidacionException e) {

            model.addAttribute("cliente", cliente);
            model.addAttribute("accion", "editar");
            model.addAttribute("error", e.getMessage());
            return "clientes/form-cliente";
        }
    }

    // ELIMINAR CLIENTE
    @GetMapping("/eliminar/{id}")
    public String eliminarCliente(@PathVariable Long id, Model model) {
        try {
            clienteService.delete(id);
            return "redirect:/clientes";

        } catch (ClienteNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "clientes/lista-clientes";
        }
    }
}


